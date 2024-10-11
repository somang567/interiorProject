package com.keduit.interiors.controller;

import com.keduit.interiors.repository.MegazineRepository;
import com.keduit.interiors.service.MegazineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/megazines")
@RequiredArgsConstructor
public class MegazineController {

  private final MegazineService boardService;
  private final MegazineRepository memberRepository;

  //list를 보여주는 부분이기 때문에 post 필요 없숨~
  @GetMapping("/list")
  //@PathVariable("page") Optional<Integer> page: URL 경로에서 페이지 번호를 직접 가져오는 방식입니다.
  public String megazineItem(Optional<Integer> page, Model model){
    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9); //한 화면에 9개의 상품
    //Page<Megazine> items = megazineService.getListItemPage(pageable);//엔티티에서 값을 service에서 가져온다. pageable이라는 매개변수만 가져와서 리스트로 뿌려준다.
    //getListItemPage(pageable);
    //model.addAttribute("boardDTO", new MegaizineDTO());
    model.addAttribute("maxPage", 5); //한 화면에 5개의 페이지네이션
    //model.addAttribute("megazineItems", megazineItems);  //boardLists.html 로 가서 리스트를 뿌려준다.
    return "megazine/megazineMain";
  }
}
