package com.keduit.interiors.controller;

import com.keduit.interiors.dto.BoardDTO;
import com.keduit.interiors.dto.MemberDTO;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.BoardService;
import com.keduit.interiors.service.MegazineScrapService;
import com.keduit.interiors.service.MegazineService;
import com.keduit.interiors.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final BoardService boardService;
  private final MegazineService megazineService;
  private final MemberService memberService;
  private final MegazineScrapService megazineScrapService;


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

  @GetMapping("/mypage")
  public String mypage(
          Principal principal,
          Model model, @AuthenticationPrincipal UserDetails userDetails,
          @PageableDefault(page = 0, size = 9, sort = "mno", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    String email = userDetails.getUsername(); // 로그인된 사용자의 이메일 가져오기
    Member member = memberService.findByEmail(email); // 이메일로 회원 조회

    if (member != null) {
      model.addAttribute("name", member.getName());
      model.addAttribute("email", member.getEmail());
    }

    //민영 스크랩
    Page<Megazine> list = megazineService.getListItemPage(pageable); // 메인페이지 리스트 부분
    model.addAttribute("list", list);
    
    //List<Megazine> scrappedMegazines = megazineScrapService.getScrappedMegazines(member.getId());
    List<Megazine> scrappedMegazines = megazineScrapService.getScrappedMegazines(Objects.requireNonNull(member).getId());
    model.addAttribute("scrappedMegazines", scrappedMegazines);

    //사용자 이름을 가져오기 위한 메서드
    if (principal != null) {
      String username = principal.getName();
      model.addAttribute("UserName", member.getName()); //이거 주석처리하니까 됨
    }
    //민영 스크랩 끝

    return "/member/mypage"; // mypage.html로 이동
  }


/*
  public String getScrappedMegazines(@PathVariable Long memberId, Model model) {
    List<Megazine> scrappedMegazines = megazineService.getScrappedMegazines(memberId);
    model.addAttribute("scrappedMegazines", scrappedMegazines);
    return "scrapView"; // 뷰 이름
  }

 */


}
