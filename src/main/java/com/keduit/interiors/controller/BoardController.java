package com.keduit.interiors.controller;

import com.keduit.interiors.dto.BoardDTO;
import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.BoardService;
import com.keduit.interiors.service.CommentService;
import com.keduit.interiors.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberService memberService;

    // 게시글 작성 폼
    @GetMapping("/board/write")
    public String boardWriteForm(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Member member = memberService.findByEmail(username);
            model.addAttribute("authorName", member.getName());
        }
        return "boards/boardwrite";
    }

    // 게시글 작성 처리
    @PostMapping("/board/writedo")
    public String boardWritePro(BoardDTO boardDTO, Model model, MultipartFile file, Principal principal) throws Exception {
        Member member = memberService.findByEmail(principal.getName());
        boardService.write(boardDTO, file, member);
        model.addAttribute("message", "게시글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "boards/message";
    }

    // 게시글 목록 조회
    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(required = false) String searchType,
                            @RequestParam(required = false) String searchKeyword) {
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

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boards/boardlist";
    }

    // 게시글 상세 조회 및 댓글 조회
    @GetMapping("/board/view")
    public String boardView(Model model, @RequestParam("id") Long id, Principal principal) {
        BoardDTO boardDTO = boardService.boardView(id);
        model.addAttribute("board", boardDTO);

        if (boardDTO.getFilename() != null) {
            String imageUrl = "/files/" + boardDTO.getFilename();
            model.addAttribute("imageUrl", imageUrl);
        }

        List<CommentDTO> comments = commentService.getCommentsByBoardId(id);
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
    }

    // 댓글 목록 조회 (Ajax 요청에 대한 응답)
    @GetMapping("/board/{boardId}/comments")
    @ResponseBody
    public ResponseEntity<?> getCommentsByBoardId(@PathVariable("boardId") Long boardId) {
        List<CommentDTO> comments = commentService.getCommentsByBoardId(boardId);
        return ResponseEntity.ok(comments);
    }

    // 게시글 삭제 처리
    @GetMapping("/board/delete/{id}")
    public String boardDelete(@PathVariable Long id, Principal principal) throws IllegalAccessException {
        if (hasPermissionToModifyOrDeleteBoard(id, principal)) {
            boardService.boardDelete(id);
            return "redirect:/board/list";
        }
        return "error/403";
    }

    // 게시글 수정 폼
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Long id, Model model, Principal principal) throws IllegalAccessException {
        if (hasPermissionToModifyOrDeleteBoard(id, principal)) {
            BoardDTO boardDTO = boardService.boardView(id);
            if (boardDTO == null) {
                return "error/404";
            }
            model.addAttribute("board", boardDTO);
            return "boards/boardmodify";
        }
        return "error/403";
    }

    // 게시글 수정 처리
    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, @ModelAttribute BoardDTO boardDTO,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "deleteFile", required = false) String deleteFile,
                              Principal principal) throws Exception {
        if (hasPermissionToModifyOrDeleteBoard(id, principal)) {
            boolean deleteExistingFile = (deleteFile != null && deleteFile.equals("on"));
            Member member = memberService.findByEmail(principal.getName());
            boardService.update(boardDTO, file, deleteExistingFile, member);
            return "redirect:/board/list?alert=update_success";
        }
        return "error/403";
    }

    // 댓글 추가 처리 (Ajax 요청에 대한 응답)
    @PostMapping("/board/{boardId}/comment")
    @ResponseBody
    public ResponseEntity<?> addComment(@PathVariable("boardId") Long boardId,
                                        @RequestBody Map<String, String> payload,
                                        Principal principal) {
        String content = payload.get("content");
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBoardId(boardId);
        commentDTO.setContent(content);

        Member member = memberService.findByEmail(principal.getName());
        commentService.saveComment(commentDTO, member);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    // 댓글 수정 처리 (Ajax 요청에 대한 응답)
    @PostMapping("/comment/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateComment(@PathVariable("id") Long id,
                                           @RequestBody Map<String, String> payload,
                                           Principal principal) throws IllegalAccessException {
        if (hasPermissionToModifyOrDeleteComment(id, principal)) {
            String content = payload.get("content");
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(id);
            commentDTO.setContent(content);

            Member member = memberService.findByEmail(principal.getName());
            commentService.updateComment(commentDTO, member);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body("권한이 없습니다.");
    }

    // 댓글 삭제 처리 (Ajax 요청에 대한 응답)
    @DeleteMapping("/comment/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id, Principal principal) throws IllegalAccessException {
        if (hasPermissionToModifyOrDeleteComment(id, principal)) {
            Member member = memberService.findByEmail(principal.getName());
            commentService.deleteComment(id, member);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body("권한이 없습니다.");
    }

    // 권한 확인 메서드 (게시글)
    private boolean hasPermissionToModifyOrDeleteBoard(Long boardId, Principal principal) {
        BoardDTO boardDTO = boardService.boardView(boardId);
        if (principal == null || boardDTO == null) {
            return false;
        }
        Member member = memberService.findByEmail(principal.getName());
        return boardDTO.getAuthorId().equals(member.getId()) || member.getRole().equals("ROLE_ADMIN");
    }

    // 권한 확인 메서드 (댓글)
    private boolean hasPermissionToModifyOrDeleteComment(Long commentId, Principal principal) {
        CommentDTO commentDTO = commentService.getCommentById(commentId);
        if (principal == null || commentDTO == null) {
            return false;
        }
        Member member = memberService.findByEmail(principal.getName());
        return commentDTO.getAuthorId().equals(member.getId()) || member.getRole().equals("ROLE_ADMIN");
    }
}
