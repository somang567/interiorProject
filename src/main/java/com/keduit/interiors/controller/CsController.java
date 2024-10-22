package com.keduit.interiors.controller;

import com.keduit.interiors.constant.CS;
import com.keduit.interiors.constant.CsWriteType;
import com.keduit.interiors.constant.Role;
import com.keduit.interiors.dto.CsDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.CsService;
import com.keduit.interiors.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CsController {

	private final CsService csService;
	private final MemberService memberService;

	@GetMapping("/cs/list")
	public String getCsList(Model model,
	                        @RequestParam(value = "page", defaultValue = "1") int page,
	                        Principal principal) {

		int pageSize = 10;  // 페이지 당 게시물 수
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		Page<CsDTO> csPage = csService.getAllCs(pageable);

		int totalPages = csPage.getTotalPages();
		int currentPage = page;
		int displayPageCount = 8;  // 한 번에 표시할 페이지 번호 개수
		int startPage = Math.max(1, currentPage - displayPageCount / 2);
		int endPage = Math.min(totalPages, startPage + displayPageCount - 1);

		model.addAttribute("csList", csPage.getContent());
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("principal", principal);  // 로그인 사용자 정보

		return "cs/csMain";
	}


	// CS 등록 페이지로 이동
	@GetMapping("/cs/write")
	public String writeCsForm(Model model, Principal principal) {
		model.addAttribute("csDTO", new CsDTO());

		// 로그인한 사용자의 역할을 전달
		String role = String.valueOf(memberService.findByEmail(principal.getName()).getRole());
		model.addAttribute("userRole", role);

		return "cs/csWriteForm";
	}


	// CS 등록 처리
	@PostMapping("/cs/write")
	public String writeCs(CsDTO csDTO, Principal principal) {
		String email = principal.getName();
		Member member = memberService.findByEmail(email);
		csDTO.setMemberId(member.getId());

		// 관리자이면 접수 상태를 설정하지 않고, 유저이면 'ACCEPT'로 설정
		if (member.getRole() == Role.USER) {
			csDTO.setCsStatus(CS.ACCEPT);
		}

		// 관리자이면 NOTICE로, 유저이면 COMMON으로 설정
		if (member.getRole() == Role.ADMIN) {
			csDTO.setCsWriteType(CsWriteType.NOTICE);
		} else {
			csDTO.setCsWriteType(CsWriteType.COMMON);
		}

		System.out.println("Role: " + member.getRole());
		System.out.println("CsWriteType set to: " + csDTO.getCsWriteType());

		csService.saveCs(csDTO);
		return "redirect:/cs/list";
	}

	// CS 상세보기 페이지
	@GetMapping("/cs/view/{id}")
	public String viewCsDetail(@PathVariable Long id, Model model, Principal principal) {
		CsDTO csDTO = csService.getCsById(id);
		model.addAttribute("csDTO", csDTO);
		model.addAttribute("principal", principal);

		// 로그인한 사용자가 작성자인지 확인 (email로 비교)
		String email = principal.getName();
		boolean isWriter = email.equals(csDTO.getMemberEmail());  // email로 비교
		model.addAttribute("isWriter", isWriter);

		return "cs/csDetailForm";
	}

	// CS 수정 페이지로 이동
	@GetMapping("/cs/edit/{id}")
	public String editCsForm(@PathVariable Long id, Model model, Principal principal) {
		CsDTO csDTO = csService.getCsById(id);
		String email = principal.getName();
		boolean isWriter = csDTO.getMemberEmail().equals(email);  // email로 비교

		if (!isWriter) {
			return "redirect:/cs/list";  // 작성자가 아닌 경우 목록으로 리다이렉트
		}

		model.addAttribute("csDTO", csDTO);
		return "cs/csModifyForm";
	}

	// CS 수정 처리
	@PostMapping("/cs/edit/{id}")
	public String editCs(@PathVariable Long id, CsDTO csDTO, Principal principal) {
		String email = principal.getName();
		csService.updateCs(id, csDTO, email);  // 로그인한 사용자의 이메일을 전달
		return "redirect:/cs/list";
	}

	// CS 삭제 처리
	@PostMapping("/cs/delete/{id}")
	public String deleteCs(@PathVariable Long id, Principal principal) {
		String email = principal.getName();
		csService.deleteCs(id, email);  // 로그인한 사용자의 이메일을 전달
		return "redirect:/cs/list";
	}
}
