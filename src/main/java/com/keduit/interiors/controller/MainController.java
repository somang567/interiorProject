package com.keduit.interiors.controller;

import com.keduit.interiors.dto.*;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.*;
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
  private final ProductService productService;
  private final CompanypromotionService companypromotionService;
  private final ProductScrapService productScrapService;
  private final SelfInteriorService selfInteriorService;

  @GetMapping("/")
  public String main(Model model,
                     @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable boardPageable) {

    // 게시판 리스트 부분
    Page<BoardDTO> list = boardService.boardList(boardPageable);
    model.addAttribute("list", list);

    // 매거진 리스트 부분 (정렬 없이 기본 페이징)
    Pageable megazinePageable = PageRequest.of(0, 5); // 기본 페이지 크기만 설정
    Page<Megazine> items = megazineService.getListItemPage(megazinePageable);

    model.addAttribute("items", items);

    // 상품 찾기 부분 (랜덤으로 상품 가져오기)
    List<ProductDTO> randomProducts = productService.getRandomProducts(4); // 원하는 개수 설정 (예: 2개)
    model.addAttribute("randomProducts", randomProducts);

    // 업체 홍보 부분 (최대 4개의 홍보 항목 가져오기)
    int maxPromotionCount = 4; // 최대 항목 수 설정
    Pageable promotionPageable = PageRequest.of(0, maxPromotionCount, Sort.by(Sort.Direction.DESC, "id"));
    Page<CompanypromotionDTO> promotions = companypromotionService.list(promotionPageable);
    model.addAttribute("promotions", promotions.getContent());

    // 셀프 인테리어 리스트 부분
    Pageable selfInteriorPageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "id")); // 최신 3개 가져오기
    Page<SelfInteriorDTO> selfInteriorsPage = selfInteriorService.list(selfInteriorPageable);
    List<SelfInteriorDTO> selfInteriors = selfInteriorsPage.getContent();
    model.addAttribute("selfInteriors", selfInteriors);

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

    // 스크랩한 상품목록 가져오기
    List<Long> scrappedProductIds = productScrapService.getScrapProductIdsForUser(member.getId());
    List<ProductDTO> scrappedProducts = productService.getProductsByIds(scrappedProductIds);

    // 썸네일 이미지 설정
    scrappedProducts.forEach(product -> {
      String firstImageUrl = productService.getFirstImageUrl(product.getId());
      product.setFirstImageUrl(firstImageUrl);  // 썸네일 이미지 설정
      model.addAttribute("scrappedProducts" , scrappedProducts);
    });

    //List<Megazine> scrappedMegazines = megazineScrapService.getScrappedMegazines(member.getId());

    //민영 스크랩

    Page<Megazine> list = megazineService.getListItemPage(pageable); // 메인페이지 리스트 부분
    model.addAttribute("list", list);
    
    List<Megazine> scrappedMegazines = megazineScrapService.getScrappedMegazines(Objects.requireNonNull(member).getId());
    model.addAttribute("scrappedMegazines", scrappedMegazines);

    // 작성한 게시글 목록 가져오기
    List<BoardDTO> myPosts = boardService.findPostsByAuthorId(member.getId());
    model.addAttribute("myPosts", myPosts);

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
