package com.keduit.interiors.controller;

import com.keduit.interiors.entity.Board;
import com.keduit.interiors.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boards/boardwrite";
    }

    @PostMapping("/board/writedo")
    public String boardWritePro(Board board, Model model) {
        boardService.write(board);
        model.addAttribute("message", "게시글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "boards/message"; // 리스트 페이지로 리다이렉션
    }

    @GetMapping("/board/list")
    public String boardList(Model model) {
        List<Board> boards = boardService.findAll(); // 게시글 목록 조회
        model.addAttribute("list", boards); // 모델에 추가
        return "boards/boardlist"; // 리스트 페이지 반환
    }

    @GetMapping("/board/view")
    public String boardview(Model model, Long id) {
        model.addAttribute("board", boardService.boardview(id));
        return "boards/boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Long id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Long id, Model model) {
        model.addAttribute("board", boardService.boardview(id));
        return "boards/boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, Board board) {
        Board boardTemp = boardService.boardview(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp);

        return "redirect:/board/list";
    }
}
