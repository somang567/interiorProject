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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        if (board.getImageFilename() != null) {
            String imageUrl = "/images/" + board.getImageFilename();  // static/images 경로에 저장된 이미지
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
        model.addAttribute("board", boardService.boardView(id));
        return "boards/boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, Board board, MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
        Board boardTemp = boardService.boardView(id);

        // 제목과 내용 업데이트
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        // 새 파일이 업로드된 경우 기존 파일을 교체
        if (file != null && !file.isEmpty()) {
            boardService.updateFile(boardTemp, file);  // 기존 파일 삭제 및 새 파일 저장
        }

        // 게시글 업데이트 (파일이 없는 경우에도 정상적으로 처리)
        boardService.update(boardTemp, file);  // 새 파일을 전달하여 게시글 업데이트

        // 수정 완료 후 알림을 위해 리다이렉트할 URL에 쿼리 매개변수 추가
        return "redirect:/board/list?alert=update_success";
    }
}
