package com.keduit.interiors.controller;

import com.keduit.interiors.dto.SearchDTO;
import com.keduit.interiors.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controller : http 요청처리 및 응답 반환. 서비스를 호출하여 비즈니스 로직을 수행하고, 결과를 클라이언트에 전달함.
@Controller
@RequiredArgsConstructor
public class SearchController {

  private final SearchService searchService;

  // 업체찾기 페이지 반환
  @GetMapping("/search")
  public String searchList(Model model) {
    // 필요한 데이터가 있다면 모델에 추가 가능
    return "/search/search";               // search 폴더에 있는 search.html로 이동
  }

  // json 결과 반환
  @GetMapping("/search/api")
  @ResponseBody
  public List<SearchDTO> searchApi(
      @RequestParam String dosi,
      @RequestParam String googun) {
    return searchService.getSearchResults(dosi, googun);    // 서비스 메소드 호출
  }
}
