package com.keduit.interiors.controller;

import com.keduit.interiors.dto.BoardDTO;
import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.service.BoardService;
import com.keduit.interiors.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    // 게시글 작성 폼
    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boards/boardwrite";
    }

    // 게시글 작성 처리
    @PostMapping("/board/writedo")
    public String boardWritePro(BoardDTO boardDTO, Model model, MultipartFile file) throws Exception {
        boardService.write(boardDTO, file);
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
        // 게시글 정보 가져오기
        BoardDTO boardDTO = boardService.boardView(id);
        model.addAttribute("board", boardDTO);

        // 댓글 리스트 가져오기
        model.addAttribute("comments", commentService.getCommentsByBoardId(id));

        // 이미지가 있는 경우 이미지 URL도 모델에 추가
        if (boardDTO.getFilename() != null) {
            String imageUrl = "/files/" + boardDTO.getFilename();
            model.addAttribute("imageUrl", imageUrl);
        }

        List<CommentDTO> comments = commentService.getCommentsByBoardId(id);

        String currentUsername = principal != null ? principal.getName() : null;

        for (CommentDTO comment : comments) {
            comment.setEditable(currentUsername != null && currentUsername.equals(comment.getAuthor()));
        }

        model.addAttribute("comments", comments);

        return "boards/boardview";
    }

    // 게시글 삭제 처리
    @GetMapping("/board/delete/{id}")
    public String boardDelete(@PathVariable Long id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    // 게시글 수정 폼
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Long id, Model model) {
        BoardDTO boardDTO = boardService.boardView(id);
        if (boardDTO == null) {
            return "error/404";
        }
        model.addAttribute("board", boardDTO);
        return "boards/boardmodify";
    }

    // 게시글 수정 처리
    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, @ModelAttribute BoardDTO boardDTO,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "deleteFile", required = false) String deleteFile) throws Exception {
        boolean deleteExistingFile = (deleteFile != null && deleteFile.equals("on"));
        boardService.update(boardDTO, file, deleteExistingFile);
        return "redirect:/board/list?alert=update_success";
    }

    // 댓글 추가 처리 (게시글 상세 페이지에서 댓글 작성)
    @PostMapping("/board/{boardId}/comment")
    public String addComment(@PathVariable("boardId") Long boardId, CommentDTO commentDTO) {
        commentDTO.setBoardId(boardId);
        commentService.saveComment(commentDTO);  // 댓글 저장
        return "redirect:/board/view?id=" + boardId;
    }

    @PostMapping("/comment/update/{id}")
    public String updateComment(@PathVariable("id") Long id, CommentDTO commentDTO, Principal principal) {
        // 댓글 작성자와 현재 사용자 확인
        CommentDTO existingComment = commentService.getCommentById(id);
        if (!existingComment.getAuthor().equals(principal.getName())) {
            // 권한 없음 처리
            return "error/403";
        }

        commentService.updateComment(commentDTO);
        return "redirect:/board/view?id=" + commentDTO.getBoardId();
    }

    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id, @RequestParam("boardId") Long boardId, Principal principal) {
        // 댓글 작성자와 현재 사용자 확인
        CommentDTO existingComment = commentService.getCommentById(id);
        if (!existingComment.getAuthor().equals(principal.getName())) {
            // 권한 없음 처리
            return "error/403";
        }

        commentService.deleteComment(id);
        return "redirect:/board/view?id=" + boardId;
    }


}