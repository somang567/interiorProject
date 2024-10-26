package com.keduit.interiors.controller;

import com.keduit.interiors.constant.Role;
import com.keduit.interiors.dto.SelfInteriorCommentDTO;
import com.keduit.interiors.dto.SelfInteriorDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.MemberService;
import com.keduit.interiors.service.SelfInteriorCommentService;
import com.keduit.interiors.service.SelfInteriorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/selfinterior")
public class SelfInteriorController {

    private static final Logger logger = LoggerFactory.getLogger(SelfInteriorController.class);

    private final SelfInteriorService selfInteriorService;
    private final SelfInteriorCommentService selfInteriorCommentService;
    private final MemberService memberService;

    @Autowired
    public SelfInteriorController(SelfInteriorService selfInteriorService, SelfInteriorCommentService selfInteriorCommentService, MemberService memberService) {
        this.selfInteriorService = selfInteriorService;
        this.selfInteriorCommentService = selfInteriorCommentService;
        this.memberService = memberService;
    }

    // 게시글 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model, Principal principal) {
        if (principal != null) {
            Member member = memberService.findByEmail(principal.getName());
            model.addAttribute("authorName", member.getName());
        }
        return "selfinteriors/selfwrite";
    }

    // 게시글 작성 처리
    @PostMapping("/writedo")
    public String writeDo(SelfInteriorDTO selfInteriorDTO, @RequestParam(value = "file", required = false) MultipartFile file, Model model, Principal principal) {
        try {
            Member member = memberService.findByEmail(principal.getName());
            selfInteriorService.save(selfInteriorDTO, file, member);
            logger.info("새 게시글이 성공적으로 저장되었습니다. 게시글 ID: {}", selfInteriorDTO.getId());
            return "redirect:/selfinterior/list";
        } catch (Exception e) {
            logger.error("게시글 저장 중 오류 발생: ", e);
            model.addAttribute("errorMessage", "게시글 저장 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public String list(Model model,
                       @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false) String searchType,
                       @RequestParam(required = false) String searchKeyword) {
        try {
            Page<SelfInteriorDTO> list;

            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                if ("title".equals(searchType)) {
                    list = selfInteriorService.searchByTitle(searchKeyword, pageable);
                } else if ("content".equals(searchType)) {
                    list = selfInteriorService.searchByContent(searchKeyword, pageable);
                } else {
                    list = selfInteriorService.searchByTitleOrContent(searchKeyword, pageable);
                }
            } else {
                list = selfInteriorService.list(pageable);
            }

            int nowPage = list.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 5, list.getTotalPages());

            // endPage가 startPage보다 작지 않도록 보장
            endPage = Math.max(endPage, startPage);

            logger.debug("nowPage: {}, startPage: {}, endPage: {}, totalPages: {}", nowPage, startPage, endPage, list.getTotalPages());

            model.addAttribute("list", list.getContent());
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("totalPages", list.getTotalPages());

            return "selfinteriors/selflist";
        } catch (Exception e) {
            logger.error("게시글 목록 조회 중 오류 발생: ", e);
            model.addAttribute("errorMessage", "게시글 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 상세 조회
    @GetMapping("/view")
    public String view(Model model, @RequestParam("id") Long id, Principal principal) {
        try {
            SelfInteriorDTO selfInteriorDTO = selfInteriorService.view(id);

            model.addAttribute("selfinterior", selfInteriorDTO);

            if (selfInteriorDTO.getFilename() != null) {
                String imageUrl = "/files/" + selfInteriorDTO.getFilename();
                model.addAttribute("imageUrl", imageUrl);
            }

            // 댓글 불러오기 로직 추가
            List<SelfInteriorCommentDTO> comments = selfInteriorCommentService.getCommentsBySelfInteriorId(id);
            model.addAttribute("comments", comments);

            return "selfinteriors/selfview";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 수정 폼
    @GetMapping("/modify/{id}")
    public String modifyForm(@PathVariable("id") Long id, Model model, Principal principal) {
        if (!hasPermissionToModifyOrDeleteBoard(id, principal)) {
            model.addAttribute("errorMessage", "권한이 없습니다. 게시글을 수정할 수 없습니다.");
            return "error/403"; // 접근 거부 페이지
        }

        try {
            SelfInteriorDTO selfInteriorDTO = selfInteriorService.view(id);

            model.addAttribute("selfinterior", selfInteriorDTO);
            return "selfinteriors/selfmodify";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정 폼 로딩 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 수정 처리
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, SelfInteriorDTO selfInteriorDTO,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam(value = "deleteFile", required = false) String deleteFile,
                         Principal principal, Model model) {
        if (!hasPermissionToModifyOrDeleteBoard(id, principal)) {
            model.addAttribute("errorMessage", "권한이 없습니다. 게시글을 수정할 수 없습니다.");
            return "error/403"; // 접근 거부 페이지
        }

        try {
            boolean deleteExistingFile = (deleteFile != null && deleteFile.equals("on"));
            Member member = memberService.findByEmail(principal.getName());
            selfInteriorService.update(selfInteriorDTO, file, deleteExistingFile, member);
            return "redirect:/selfinterior/view?id=" + id;
        } catch (AccessDeniedException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/403"; // 접근 거부 페이지
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 삭제 처리
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        if (!hasPermissionToModifyOrDeleteBoard(id, principal)) {
            redirectAttributes.addFlashAttribute("error", "권한이 없습니다. 게시글을 삭제할 수 없습니다.");
            return "redirect:/selfinterior/list"; // 권한이 없을 경우 목록으로 리다이렉트
        }

        try {
            Member member = memberService.findByEmail(principal.getName());
            selfInteriorService.delete(id, member);
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다.");
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "게시글 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/selfinterior/list"; // 삭제 성공 후 목록으로 리다이렉트
    }

    // 댓글 추가 처리 (AJAX 요청)
    @PostMapping("/comment/add")
    @ResponseBody
    public ResponseEntity<?> addComment(@RequestBody SelfInteriorCommentDTO commentDTO, Principal principal) {
        if (principal == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        try {
            Member member = memberService.findByEmail(principal.getName());
            SelfInteriorCommentDTO savedComment = selfInteriorCommentService.addComment(commentDTO, member);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "댓글 추가 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // 댓글 수정 처리 (AJAX 요청)
    @PostMapping("/comment/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateComment(@PathVariable("id") Long id,
                                           @RequestBody SelfInteriorCommentDTO commentDTO,
                                           Principal principal) {
        if (principal == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        try {
            // 권한 검증은 서비스 레이어에서 처리됨
            selfInteriorCommentService.updateComment(commentDTO, memberService.findByEmail(principal.getName()));
            return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
        } catch (AccessDeniedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "댓글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // 댓글 삭제 처리 (AJAX 요청)
    @DeleteMapping("/comment/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        try {
            // 권한 검증은 서비스 레이어에서 처리됨
            selfInteriorCommentService.deleteComment(id, memberService.findByEmail(principal.getName()));
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "댓글이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(successResponse);
        } catch (AccessDeniedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "댓글 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // 권한 확인 메서드 (게시글)
    private boolean hasPermissionToModifyOrDeleteBoard(Long boardId, Principal principal) {
        if (principal == null) {
            return false;
        }
        SelfInteriorDTO selfInteriorDTO = selfInteriorService.view(boardId);
        if (selfInteriorDTO == null) {
            return false;
        }
        Member member = memberService.findByEmail(principal.getName());
        return selfInteriorDTO.getAuthorId().equals(member.getId()) || member.getRole() == Role.ADMIN;
    }

    // 예외 처리 핸들러
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/selfinterior/list";
    }

}
