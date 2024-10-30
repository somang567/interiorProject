package com.keduit.interiors.controller;

import com.keduit.interiors.constant.CS;
import com.keduit.interiors.dto.CsCommentDTO;
import com.keduit.interiors.dto.CsDTO;
import com.keduit.interiors.service.CsCommentService;
import com.keduit.interiors.service.CsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/csComment")
@RequiredArgsConstructor
public class CsCommentController {

	private final CsCommentService csCommentService;
	private final CsService csService;
	// 댓글 추가 (리다이렉트 방식으로 처리)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public String addComment(@ModelAttribute CsCommentDTO csCommentDTO, Principal principal) {
		// 로그인한 사용자의 이메일로 댓글 추가
		csCommentService.addComment(csCommentDTO, principal.getName());
		csService.updateCsStatus(csCommentDTO.getCsId(), CS.SUCCESS);
		// 댓글 작성 후 해당 게시물의 상세보기 페이지로 리다이렉트
		return "redirect:/view/" + csCommentDTO.getCsId();  // 상세보기 페이지로 리다이렉트
	}

	// 댓글 수정 페이지로 이동
	@GetMapping("/edit/{id}")
	public String editCommentForm(@PathVariable Long id, Model model) {
		CsCommentDTO commentDTO = csCommentService.getCommentById(id);
		model.addAttribute("csCommentDTO", commentDTO);  // 템플릿에서 사용될 변수명과 일치시킴
		return "cs/csCommentModifyForm";  // 수정할 댓글 정보를 담은 페이지로 이동
	}


	// 댓글 수정 처리
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/edit/{id}")
	public String updateComment(@PathVariable Long id, @ModelAttribute CsCommentDTO csCommentDTO, Principal principal) {
		try {
			// 댓글 수정 로직 처리
			csCommentService.updateComment(id, csCommentDTO, principal.getName());
		} catch (NumberFormatException e) {
			// 예외 발생 시 다시 수정 페이지로 리다이렉트
			System.out.println("NumberFormatException 발생: " + e.getMessage());
			return "redirect:/csComment/edit/" + id;  // 수정 페이지로 다시 리다이렉트
		}

		return "redirect:/view/" + csCommentDTO.getCsId();  // 수정 후 상세보기로 리다이렉트
	}



	// 댓글 삭제 처리
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/delete/{id}")
	public String deleteComment(@PathVariable Long id, Principal principal) {
		// 댓글 삭제 로직 처리
		CsCommentDTO commentDTO = csCommentService.getCommentById(id);
		csCommentService.deleteComment(id, principal.getName());
		return "redirect:/view/" + commentDTO.getCsId();  // 삭제 후 상세보기로 리다이렉트
	}

	// 댓글 목록 조회 (자동으로 JSON 변환)
	@GetMapping("/list/{csId}")
	@ResponseBody
	public List<CsCommentDTO> getComments(@PathVariable Long csId) {
		return csCommentService.getCommentsByCsId(csId);  // Spring이 자동으로 JSON으로 변환
	}
}
