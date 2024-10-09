package com.keduit.interiors.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

/*  @GetMapping("/")
  public String main(Optional<Integer> page, Model model) {
    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
    *//*model.addAttribute("item", items);*//*

    return "main";
  }*/

  @GetMapping("/")
  public String main() {

    return "main";
  }
}
