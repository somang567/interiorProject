package com.keduit.interiors.controller;

import com.keduit.interiors.dto.BoardDTO;
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

    // 게시글 작성 폼
    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boards/boardwrite";
    }

    // 게시글 작성 처리
    @PostMapping("/board/writedo")
    public String boardWritePro(BoardDTO boardDTO, Model model, MultipartFile file) throws Exception {
        // 게시글 작성 및 첨부파일(이미지) 저장 (DTO를 사용)
        boardService.write(boardDTO, file);

        model.addAttribute("message", "게시글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "boards/message";
    }

    // 게시글 목록 조회
    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {
        Page<BoardDTO> list;
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

    // 게시글 상세 조회
    @GetMapping("/board/view")
    public String boardView(Model model, Long id) {
        // 게시글 및 첨부파일(이미지) 정보 가져오기 (DTO로 변환)
        BoardDTO boardDTO = boardService.boardView(id);
        model.addAttribute("board", boardDTO);

        // 이미지가 있는 경우 이미지 URL도 모델에 추가
        if (boardDTO.getFilename() != null) {
            String imageUrl = "/files/" + boardDTO.getFilename();
            model.addAttribute("imageUrl", imageUrl);
        }

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
            // 게시글이 없을 경우 에러 페이지로 이동하거나 적절한(?)처리
            return "error/404";
        }
        model.addAttribute("board", boardDTO);
        return "boards/boardmodify";
    }

    // 게시글 수정 처리
    @PostMapping("/board/update/{id}")
    public String boardUpdate(
            @PathVariable("id") Long id,
            @ModelAttribute BoardDTO boardDTO,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "deleteFile", required = false) String deleteFile,
            RedirectAttributes redirectAttributes) throws Exception {

        boolean deleteExistingFile = (deleteFile != null && deleteFile.equals("on"));

        // 게시글 업데이트 로직 (DTO를 사용)
        boardService.update(boardDTO, file, deleteExistingFile);

        // 수정 완료 후 리다이렉트
        return "redirect:/board/list?alert=update_success";
    }
}
