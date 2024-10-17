package com.keduit.interiors.controller;

import com.keduit.interiors.dto.BoardDTO;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.service.BoardService;
import com.keduit.interiors.service.MegazineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final BoardService boardService;
  private final MegazineService megazineService;


  @GetMapping("/")
  public String main(Model model,
                     @PageableDefault(page = 0, size = 10) Pageable boardPageable) {

    // 게시판 리스트 부분
    Page<BoardDTO> list = boardService.boardList(boardPageable);
    model.addAttribute("list", list);

    // 매거진 리스트 부분 (정렬 없이 기본 페이징)
    Pageable megazinePageable = PageRequest.of(0, 5); // 기본 페이지 크기만 설정
    Page<Megazine> items = megazineService.getListItemPage(megazinePageable);
    model.addAttribute("items", items);

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
