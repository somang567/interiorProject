
package com.keduit.interiors.controller;

import com.keduit.interiors.dto.ItemSearchDTO;
import com.keduit.interiors.dto.MegazineDTO;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.repository.MegazineRepository;
import com.keduit.interiors.service.MegazineService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/megazines")
@RequiredArgsConstructor
public class MegazineController {

  private final MegazineService megazineService;
  private final MegazineRepository megazineRepository;

  @GetMapping("/list")
  public String megazineItem(@RequestParam("searchKeyword") String searchKeyword,
                             @PageableDefault(page= 0, size=9, sort="mno", direction = Sort.Direction.DESC) Pageable pageable,
                             Model model) {

    Page<Megazine> list = null;

    if (searchKeyword == null) {
      list = megazineService.getListItemPage(pageable); // 메인페이지 리스트 부분
      model.addAttribute("list", list);

    }else{
      list = megazineService.megazineSearchList(searchKeyword, pageable); // 메인페이지 리스트 부분
      model.addAttribute("list", list);
    }

    int nowPage = list.getPageable().getPageNumber() + 1; //pageable에서 넘어온 현재 페이지를 반환/ 페이지 1부터 시작.
    int startPage = Math.max(nowPage - 4 , 1);
    int endPage = Math.min(nowPage + 5, list.getTotalPages());
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);


    long totalCnt = megazineService.countTotalMagazines();  //전체 매거진 개수
    model.addAttribute("totalCnt", totalCnt);


    model.addAttribute("searchKeyword", searchKeyword); // 검색어를 모델에 추가
    model.addAttribute("maxPage", 5); // 한 화면에 5개의 페이지네이션
    return "megazine/megazineMain";
  }


  //상품 등록========================================================
  @GetMapping("/user/write/new")
  public String megazineNew(Model model){
    model.addAttribute("megazineDTO", new MegazineDTO());
    return "megazine/megazineForm";
  }

  //상품 등록 데이터 서버에 전달
  @PostMapping("/user/write/new")
  //@Valid 유효성 검사 수행
  //bindingResult: 유효성 검사 결과를 담고 있는 객체로, 오류가 발생하면 이를 확인할 수 있습니다.
  //model: 뷰에 데이터를 전달하는 데 사용되는 객체입니다.
  //itemImgFileList: 사용자가 업로드한 이미지 파일 리스트를 나타냅니다.
  public String itemNew(@Valid MegazineDTO megazineDTO, BindingResult bindingResult, Model model,
                        @RequestParam("itemImgFile") MultipartFile itemImgFile) {
           
    //유효성 검사에서 에러가 발생한 경우, 사용자에게 폼을 다시 보여줍니다. 이때 itemForm.html이 반환됩니다.
    if (bindingResult.hasErrors()) {
      return "megazine/megazineForm"; //item에 있는 itemForm.html
    }

    //이미지 하나라도 안넣었다면 안넘어간다~
    //이미지 파일 리스트의 첫 번째 파일이 비어 있고, itemDTO의 ID가 null인 경우(즉, 새로 생성하는 경우) 에러 메시지를 모델에 추가하고 폼으로 돌아갑니다.
    if (itemImgFile.isEmpty()) {
      model.addAttribute("errorMessage", "상품 이미지는 필수 입력 입니다.");
      return "megazine/megazineForm";
    }


    try {
      megazineService.saveItem(megazineDTO, itemImgFile);

    } catch (Exception e) {
      model.addAttribute("errorMessage", "매거진 등록 중 에러가 발생하였습니다.");
      System.out.println("====================================");
      e.printStackTrace();
      return "megazine/megazineForm";
    }
    return "redirect:/megazines/list";  //상품등록이 잘 되면 메인으로 이동
  }


// 상세보기 관련 Url --------
  //User 매거진 상세보기 페이지
  @GetMapping("/list/{megazineId}")
  public String itemDtl(Model model, @PathVariable("megazineId") Long megazineId){

    MegazineDTO megazineDTO = megazineService.getItemDtl(megazineId);
    model.addAttribute("megazineDTO", megazineDTO);
    return "megazine/megazineUserDetail";
  }

  @GetMapping("/edit/{megazineId}")
  public String itemEdit(Model model, @PathVariable("megazineId") Long megazineId){

    MegazineDTO megazineDTO = megazineService.getItemDtl(megazineId);
    model.addAttribute("megazineDTO", megazineDTO);
    return "megazine/megazineForm";
  }

  //Admin 매거진 상세보기 페이지
  //url path에 있는 것을 변수로 쓰겠어
  @GetMapping("admin/item/{megazineId}")
  public String itemDtl(@PathVariable("megazineId") Long megazineId, Model model) {

    try {
      MegazineDTO megazineDTO = megazineService.getItemDtl(megazineId);
      model.addAttribute("megazineDTO", megazineDTO);
    } catch (EntityNotFoundException e) {
      model.addAttribute("errorMessaage", "존재하지 않는 상품 입니다");
      model.addAttribute("megazineDTO", new MegazineDTO());
    }
    return "megazine/megazineDetail"; //버튼 있으면 저장 없으면 수정버튼이 보임 itemForm으로 똑같이 보내기로 함
  }


  @PostMapping("admin/item/{megazineId}")
  public String itemUpdate(@Valid MegazineDTO megazineDTO,
                           BindingResult bindingResult,
                           @RequestParam("itemImgFile") MultipartFile itemImgFileList,
                           Model model) {
    if (bindingResult.hasErrors()) {
      return "megazine/megazineDetail"; //에러 나면 다시
    }
    //비어 있으면
    if (itemImgFileList.isEmpty() && megazineDTO.getMno() == null) {
      model.addAttribute("errorMessage", "첫번재 상품 이미지는 필수입력입니다.");
      return  "megazine/megazineDetail";
    }


    try {
      megazineService.updateItemImg(itemImgFileList,megazineDTO);
    } catch (Exception e){
      System.out.println(e.getMessage()); //에러 메시지 확인
      model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
      return "megazine/megazineForm";
    }

    //수정 후 메인으로 감
    return "redirect:/megazines/list";
  }




















}

