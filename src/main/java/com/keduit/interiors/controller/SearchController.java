package com.keduit.interiors.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controller : http 요청처리 및 응답 반환. 서비스를 호출하여 비즈니스 로직을 수행하고, 결과를 클라이언트에 전달함.
@Controller
@RequiredArgsConstructor
public class SearchController {

  @GetMapping("/search")                // 업체찾기 페이지
  public String searchList() {
    return "/search/search";            // search 폴더에 search.html로
  }
}
