package com.keduit.interiors.controller;

import com.keduit.interiors.entity.Board;
import com.keduit.interiors.service.BoardService;
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

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boards/boardwrite";
    }

    @PostMapping("/board/writedo")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {
        // 게시글 작성 및 첨부파일(이미지) 저장
        boardService.write(board, file);

        model.addAttribute("boardId", board.getId());
        model.addAttribute("message", "게시글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "boards/message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {
        Page<Board> list;
        if (searchKeyword != null) {
            list = boardService.boardSearchList(searchKeyword, pageable);
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

    @GetMapping("/board/view")
    public String boardView(Model model, Long id) {
        // 게시글 및 첨부파일(이미지) 정보 가져오기
        Board board = boardService.boardView(id);
        model.addAttribute("board", board);

        // 이미지가 있는 경우 이미지 URL도 모델에 추가
        if (board.getFilename() != null) {
            String imageUrl = "/files/" + board.getFilename();
            model.addAttribute("imageUrl", imageUrl);
        }

        return "boards/boardview";
    }

    @GetMapping("/board/delete/{id}")
    public String boardDelete(@PathVariable Long id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Long id, Model model) {
        Board board = boardService.boardView(id);
        if (board == null) {
            // 게시글이 없을 경우 에러 페이지로 이동하거나 적절한 처리를 합니다.
            return "error/404";
        }
        model.addAttribute("board", board);
        return "boards/boardmodify";
    }


    @PostMapping("/board/update/{id}")
    public String boardUpdate(
            @PathVariable("id") Long id,
            @ModelAttribute Board board,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "deleteFile", required = false) String deleteFile,
            RedirectAttributes redirectAttributes) throws Exception {

        boolean deleteExistingFile = (deleteFile != null && deleteFile.equals("on"));

        // 게시글 업데이트 로직
        boardService.update(board, file, deleteExistingFile);

        // 수정 완료 후 리다이렉트
        return "redirect:/board/list?alert=update_success";
    }
}
