
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
  private final MegazineRepository memberRepository;

  //list를 보여주는 부분이기 때문에 post 필요 없숨~
  @GetMapping("/items")
  //@PathVariable("page") Optional<Integer> page: URL 경로에서 페이지 번호를 직접 가져오는 방식입니다.
  public String megazineItem(ItemSearchDTO itemSearchDTO, Optional<Integer> page, Model model){
    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9); //한 화면에 9개의 상품
    Page<Megazine> items = megazineService.getListItemPage(pageable);//엔티티에서 값을 service에서 가져온다. pageable이라는 매개변수만 가져와서 리스트로 뿌려준다.
    model.addAttribute("itemSearchDTO", itemSearchDTO);
    model.addAttribute("maxPage", 9); //한 화면에 5개의 페이지네이션
    model.addAttribute("megazineItems", items);  //boardLists.html 로 가서 리스트를 뿌려준다.
    return "megazine/megazineMain";
  }

  //상품 등록
  @GetMapping("/user/write/new")
  public String magazineNew(Model model){
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

    /*
      public String itemNew(@Valid MegazineDTO megazineDTO, BindingResult bindingResult, Model model,
                        @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

     */


    //유효성 검사에서 에러가 발생한 경우, 사용자에게 폼을 다시 보여줍니다. 이때 itemForm.html이 반환됩니다.
    if (bindingResult.hasErrors()) {
      return "megazine/megazineForm"; //item에 있는 itemForm.html
    }

    //제대로 안넣었다면
    //이미지 하나라도 안넣었다면 안넘어간다~
    //이미지 파일 리스트의 첫 번째 파일이 비어 있고, itemDTO의 ID가 null인 경우(즉, 새로 생성하는 경우) 에러 메시지를 모델에 추가하고 폼으로 돌아갑니다.
    if (megazineDTO.getImageUrl().isEmpty() && megazineDTO.getMno() == null) {
      model.addAttribute("errorMessage", "상품 이미지는 필수 입력 입니다.");
      return "megazine/megazineForm";
    }

    // 앞에서 계속 익셉션으로 넘겼기 때문에
    try {
      megazineService.saveItem(megazineDTO, itemImgFile);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
      System.out.println("====================================");
      e.printStackTrace();
      return "megazine/megazineForm";
    }
    return "redirect:/megazines/list";  //상품등록이 잘 되면 메인으로 이동
  }


  //url path에 있는 것을 변수로 쓰겠어
  @GetMapping("/item/{megazineId}")
  public String itemDtl(@PathVariable("megazineId") Long itemId, Model model) {
    try {
      MegazineDTO megazineDTO = megazineService.getItemDtl(itemId);
      model.addAttribute("megazineDTO", megazineDTO);
    } catch (EntityNotFoundException e) {
      model.addAttribute("errorMessaage", "존재하지 않는 상품 입니다");
      model.addAttribute("megazineDTO", new MegazineDTO());
    }
    return "megazine/megazineForm"; //버튼 있으면 저장 없으면 수정버튼이 보임 itemForm으로 똑같이 보내기로 함
  }


  @PostMapping("user/item/{itemId}")
  public String itemUpdate(@Valid MegazineDTO megazineDTO,
                           BindingResult bindingResult,
                           @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                           Model model) {
    if (bindingResult.hasErrors()) {
      return "megazine/megazineForm"; //에러 나면 다시
    }
    //비어 있으면
    if (itemImgFileList.get(0).isEmpty() && megazineDTO.getMno() == null) {
      model.addAttribute("errorMessage", "첫번재 상품 이미지는 필수입력입니다.");
      return  "megazine/megazineForm";
    }

    try {
      megazineService.updateItem(megazineDTO, itemImgFileList);
    } catch (Exception e){
      System.out.println(e.getMessage()); //에러 메시지 확인
      model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
      return "megazine/megazineForm";
    }
    //수정 후 메인으로 감
    return "redirect:/megazines/list";
  }

























}

