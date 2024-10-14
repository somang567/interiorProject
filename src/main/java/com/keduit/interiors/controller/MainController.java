package com.keduit.interiors.controller;

import com.keduit.interiors.entity.Board;
import com.keduit.interiors.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final BoardService boardService;


  @GetMapping("/")
  public String main(@RequestParam(defaultValue = "0") int page, Model model) {

    // 리스트 부분
    Pageable pageable = PageRequest.of(page, 10);  // 페이지당 10개씩 출력
    Page<Board> list = boardService.boardList(pageable);

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
