package com.keduit.interiors.controller;

import com.keduit.interiors.dto.BoardDTO;
import com.keduit.interiors.entity.Board;
import com.keduit.interiors.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final BoardService boardService;


  @GetMapping("/")
  public String main(Model model,
                     @PageableDefault(page = 0, size = 10, sort = "id",
                         direction = Sort.Direction.DESC) Pageable pageable) {

    // 리스트 부분
    Page<BoardDTO> list = boardService.boardList(pageable);

    model.addAttribute("list", list);
    return "main";  // main.html로 이동
  }

/*  @GetMapping("/")
  public String main() {

    return "main";
  }*/

  @GetMapping("/mypage")
  public String mypage() {
    return "member/mypage";
  }
}
