package com.keduit.interiors.controller;

import com.keduit.interiors.dto.CompanypromotionDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.CompanypromotionService;
import com.keduit.interiors.service.CommentService;
import com.keduit.interiors.service.CommentServiceImpl;
import com.keduit.interiors.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/companypromotion")
public class CompanypromotionController {

    @Autowired
    private CompanypromotionService companypromotionService;

    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CommentService commentService;

    // 게시글 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Member member = memberService.findByEmail(username);
            model.addAttribute("authorName", member.getName());
        }
        return "companypromotions/companywrite";
    }

    // 게시글 작성 처리 (폼 제출)
    @PostMapping("/writedo")
    public String writePro(CompanypromotionDTO companypromotionDTO, Model model, MultipartFile file, Principal principal) {
        if (principal == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "error/401"; // 로그인 필요 페이지
        }

        try {
            Member member = memberService.findByEmail(principal.getName());
            companypromotionService.write(companypromotionDTO, file, member);
            model.addAttribute("message", "게시글 작성이 완료되었습니다.");
            model.addAttribute("searchUrl", "/companypromotion/list");
            return "companypromotions/message";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 작성 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 목록 조회 (뷰 반환)
    @GetMapping("/list")
    public String list(Model model,
                       @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false) String searchType,
                       @RequestParam(required = false) String searchKeyword) {
        try {
            Page<CompanypromotionDTO> list;

            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                if ("title".equals(searchType)) {
                    list = companypromotionService.searchByTitle(searchKeyword, pageable);
                } else if ("content".equals(searchType)) {
                    list = companypromotionService.searchByContent(searchKeyword, pageable);
                } else {
                    list = companypromotionService.searchByTitleOrContent(searchKeyword, pageable);
                }
            } else {
                list = companypromotionService.list(pageable);
            }

            int nowPage = list.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 5, list.getTotalPages());

            model.addAttribute("list", list.getContent());
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("totalPages", list.getTotalPages());

            return "companypromotions/companylist";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 상세 조회 (뷰 반환)
    @GetMapping("/view")
    public String view(Model model, @RequestParam("id") Long id, Principal principal) {
        try {
            CompanypromotionDTO companypromotionDTO = companypromotionService.view(id);

            model.addAttribute("companypromotion", companypromotionDTO);

            if (companypromotionDTO.getFilename() != null) {
                String imageUrl = "/files/" + companypromotionDTO.getFilename();
                model.addAttribute("imageUrl", imageUrl);
            }

            // 조회수 증가
            companypromotionService.incrementViewCount(id);

            return "companypromotions/companyview";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 삭제 처리
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        if (!hasPermissionToModifyOrDelete(id, principal)) {
            redirectAttributes.addFlashAttribute("error", "권한이 없습니다. 게시글을 삭제할 수 없습니다.");
            return "redirect:/companypromotion/list"; // 권한이 없을 경우 목록으로 리다이렉트
        }

        try {
            companypromotionService.delete(id);
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "게시글 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/companypromotion/list"; // 삭제 성공 후 목록으로 리다이렉트
    }

    // 게시글 수정 처리 (폼 제출)
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, CompanypromotionDTO companypromotionDTO,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam(value = "deleteFile", required = false) String deleteFile,
                         Principal principal, Model model) {
        if (!hasPermissionToModifyOrDelete(id, principal)) {
            model.addAttribute("errorMessage", "권한이 없습니다. 게시글을 수정할 수 없습니다.");
            return "error/403"; // 
        }

        try {
            boolean deleteExistingFile = (deleteFile != null && deleteFile.equals("on"));
            Member member = memberService.findByEmail(principal.getName());
            companypromotionService.update(companypromotionDTO, file, deleteExistingFile, member);
            model.addAttribute("message", "게시글 수정이 완료되었습니다.");
            model.addAttribute("searchUrl", "/companypromotion/view?id=" + id);
            return "companypromotions/message";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 수정 폼 (뷰 반환)
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, Model model, Principal principal) {
        if (!hasPermissionToModifyOrDelete(id, principal)) {
            model.addAttribute("errorMessage", "권한이 없습니다. 게시글을 수정할 수 없습니다.");
            return "error/403"; // 접근 거부 페이지
        }

        try {
            CompanypromotionDTO companypromotionDTO = companypromotionService.view(id);

            model.addAttribute("companypromotion", companypromotionDTO);
            return "companypromotions/companymodify";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정 폼 로딩 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 권한 확인 메서드
    private boolean hasPermissionToModifyOrDelete(Long id, Principal principal) {
        if (principal == null) {
            return false;
        }
        CompanypromotionDTO companypromotionDTO = companypromotionService.view(id);
        if (companypromotionDTO == null) {
            return false;
        }
        Member member = memberService.findByEmail(principal.getName());
        return companypromotionDTO.getAuthorId().equals(member.getId()) || "ROLE_ADMIN".equals(member.getRole());
    }
}
