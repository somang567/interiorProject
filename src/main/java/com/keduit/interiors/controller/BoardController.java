package com.keduit.interiors.controller;

import com.keduit.interiors.dto.BoardDTO;
import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.BoardService;
import com.keduit.interiors.service.CommentService;
import com.keduit.interiors.service.CommentServiceImpl;
import com.keduit.interiors.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CommentService commentService;


    // 게시글 작성 폼
    @GetMapping("/write")
    public String boardWriteForm(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Member member = memberService.findByEmail(username);
            model.addAttribute("authorName", member.getName());
        }
        return "boards/boardwrite";
    }

    // 게시글 작성 처리 (폼 제출)
    @PostMapping("/writedo")
    public String boardWritePro(BoardDTO boardDTO, Model model, MultipartFile file, Principal principal) {
        if (principal == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "error/401"; // 로그인 필요 페이지
        }

        try {
            Member member = memberService.findByEmail(principal.getName());
            boardService.write(boardDTO, file, member);
            model.addAttribute("message", "게시글 작성이 완료되었습니다.");
            model.addAttribute("searchUrl", "/board/list");
            return "boards/message";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 작성 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 목록 조회 (뷰 반환)
    @GetMapping("/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(required = false) String searchType,
                            @RequestParam(required = false) String searchKeyword) {
        try {
            Page<BoardDTO> list;

            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                if ("title".equals(searchType)) {
                    list = boardService.boardSearchByTitle(searchKeyword, pageable);
                } else if ("content".equals(searchType)) {
                    list = boardService.boardSearchByContent(searchKeyword, pageable);
                } else {
                    list = boardService.boardSearchByTitleOrContent(searchKeyword, pageable);
                }
            } else {
                list = boardService.boardList(pageable);
            }

            int nowPage = list.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage + 5, list.getTotalPages());

            model.addAttribute("list", list.getContent());
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("totalPages", list.getTotalPages());

            return "boards/boardlist";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 상세 조회 (뷰 반환)
    @GetMapping("/view")
    public String boardView(Model model, @RequestParam("id") Long id, Principal principal) {
        try {
            BoardDTO boardDTO = boardService.boardView(id);
            if (boardDTO == null) {
                model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
                return "error/404"; // 게시글 없음 페이지
            }
            model.addAttribute("board", boardDTO);

            if (boardDTO.getFilename() != null) {
                String imageUrl = "/files/" + boardDTO.getFilename();
                model.addAttribute("imageUrl", imageUrl);
            }

            List<CommentDTO> comments = commentServiceImpl.getCommentsByBoardId(id);
            Long currentUserId = null;
            if (principal != null) {
                Member member = memberService.findByEmail(principal.getName());
                currentUserId = member.getId();
            }

            for (CommentDTO comment : comments) {
                comment.setEditable(currentUserId != null && currentUserId.equals(comment.getAuthorId()));
            }

            model.addAttribute("comments", comments);

            return "boards/boardview";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 댓글 추가 처리 (AJAX 요청)
    @PostMapping("/{boardId}/comment")
    @ResponseBody
    public ResponseEntity<?> addComment(@PathVariable("boardId") Long boardId,
                                        @RequestBody Map<String, String> payload,
                                        Principal principal) {
        if (principal == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        try {
            String content = payload.get("content");
            if (content == null || content.trim().isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "댓글 내용을 입력하세요.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setBoardId(boardId);
            commentDTO.setContent(content);

            Member member = memberService.findByEmail(principal.getName());
            CommentDTO savedCommentDTO = commentService.saveComment(commentDTO, member);

            // 댓글이 제대로 저장된 경우 반환
            return ResponseEntity.ok(savedCommentDTO);
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
                                           @RequestBody Map<String, String> payload,
                                           Principal principal) {
        if (!hasPermissionToModifyOrDeleteComment(id, principal)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "권한이 없습니다. 댓글을 수정할 수 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        try {
            String content = payload.get("content");
            if (content == null || content.trim().isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "댓글 내용을 입력하세요.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            CommentDTO commentDTO = commentServiceImpl.getCommentById(id);
            commentDTO.setContent(content);

            Member member = memberService.findByEmail(principal.getName());
            commentServiceImpl.updateComment(commentDTO, member);

            return ResponseEntity.ok(commentDTO);
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
        if (!hasPermissionToModifyOrDeleteComment(id, principal)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "권한이 없습니다. 댓글을 삭제할 수 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        try {
            commentServiceImpl.deleteComment(id, memberService.findByEmail(principal.getName()));
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "댓글이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "댓글 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // 게시글 삭제 처리
    @PostMapping("/delete/{id}")
    public String boardDelete(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        if (!hasPermissionToModifyOrDeleteBoard(id, principal)) {
            // 권한이 없을 때 에러 메시지와 함께 리다이렉트
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다. 게시글을 삭제할 수 없습니다.");
            return "redirect:/board/list";
        }

        try {
            // 게시글 삭제 처리
            boardService.boardDelete(id);
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다.");
            return "redirect:/board/list"; // 삭제 후 게시글 목록으로 리다이렉트
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/board/list"; // 에러 발생 시에도 목록 페이지로 리다이렉트
        }
    }


    // 게시글 수정 처리 (폼 제출)
    @PostMapping("/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, BoardDTO boardDTO,
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
            boardService.update(boardDTO, file, deleteExistingFile, member);
            model.addAttribute("message", "게시글 수정이 완료되었습니다.");
            model.addAttribute("searchUrl", "/board/view?id=" + id);
            return "boards/message";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 게시글 수정 폼 (뷰 반환)
    @GetMapping("/modify/{id}")
    public String boardModify(@PathVariable("id") Long id, Model model, Principal principal) {
        if (!hasPermissionToModifyOrDeleteBoard(id, principal)) {
            model.addAttribute("errorMessage", "권한이 없습니다. 게시글을 수정할 수 없습니다.");
            return "error/403"; // 접근 거부 페이지
        }

        try {
            BoardDTO boardDTO = boardService.boardView(id);
            if (boardDTO == null) {
                model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
                return "error/404"; // 게시글 없음 페이지
            }
            model.addAttribute("board", boardDTO);
            return "boards/boardmodify";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정 폼 로딩 중 오류가 발생했습니다: " + e.getMessage());
            return "error/500"; // 서버 오류 페이지
        }
    }

    // 권한 확인 메서드 (게시글)
    private boolean hasPermissionToModifyOrDeleteBoard(Long boardId, Principal principal) {
        if (principal == null) {
            return false;
        }
        BoardDTO boardDTO = boardService.boardView(boardId);
        if (boardDTO == null) {
            return false;
        }
        Member member = memberService.findByEmail(principal.getName());
        return boardDTO.getAuthorId().equals(member.getId()) || "ROLE_ADMIN".equals(member.getRole());
    }

    // 권한 확인 메서드 (댓글)
    private boolean hasPermissionToModifyOrDeleteComment(Long commentId, Principal principal) {
        if (principal == null) {
            return false;
        }
        CommentDTO commentDTO = commentServiceImpl.getCommentById(commentId);
        if (commentDTO == null) {
            return false;
        }
        Member member = memberService.findByEmail(principal.getName());
        return commentDTO.getAuthorId().equals(member.getId()) || "ROLE_ADMIN".equals(member.getRole());
    }
}
