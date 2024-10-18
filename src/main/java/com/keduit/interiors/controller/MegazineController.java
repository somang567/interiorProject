
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
  @GetMapping("/list")
  //@PathVariable("page") Optional<Integer> page: URL 경로에서 페이지 번호를 직접 가져오는 방식입니다.
  public String megazineItem(ItemSearchDTO itemSearchDTO, @PathVariable("page")Optional<Integer> page, Model model){


    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9); //한 화면에 9개의 상품
    Page<Megazine> items = megazineService.getListItemPage(pageable);//엔티티에서 값을 service에서 가져온다. pageable이라는 매개변수만 가져와서 리스트로 뿌려준다.
    //Page<Megazine> page_items = megazineService.getAdminItemPage(itemSearchDTO, pageable); //얘가 그 유명한 items임
    //model.addAttribute("items", page_items);
    //model.addAttribute("itemSearchDTO", itemSearchDTO);
    model.addAttribute("maxPage", 9); //한 화면에 5개의 페이지네이션
    model.addAttribute("megazineItems", items);  //boardLists.html 로 가서 리스트를 뿌려준다.
    return "megazine/megazineMain";
  }


  //상품 등록========================================================
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

    //유효성 검사에서 에러가 발생한 경우, 사용자에게 폼을 다시 보여줍니다. 이때 itemForm.html이 반환됩니다.
    if (bindingResult.hasErrors()) {
      return "megazine/megazineForm"; //item에 있는 itemForm.html
    }

    //제대로 안넣었다면
    //이미지 하나라도 안넣었다면 안넘어간다~
    //이미지 파일 리스트의 첫 번째 파일이 비어 있고, itemDTO의 ID가 null인 경우(즉, 새로 생성하는 경우) 에러 메시지를 모델에 추가하고 폼으로 돌아갑니다.
    if (itemImgFile.isEmpty()) {
      model.addAttribute("errorMessage", "상품 이미지는 필수 입력 입니다.");
      return "megazine/megazineForm";
    }

    // 앞에서 계속 익셉션으로 넘겼기 때문에
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

// 게시물 수정 버튼 이동=================================================================================


  @GetMapping("/register")
  public String magazineRegister(Model model){
    return "megazine/megazineRegister";
  }

  //기존에 글이 있다면 ~그대로 받아와서 넣어주고
  //
  @GetMapping("/edit")
  public String magazineEdit(Model model, MegazineDTO megazineDTO){

    //기존의 mno의 글을 가져와
    return "megazine/megazineForm";
  }

  @PostMapping("/edit")
  public String magazineEdit2(Model model, MegazineDTO megazineDTO){

    //기존의 mno의 글을 가져와
    megazineService.getMegazine(megazineDTO.getMno());
    model.addAttribute("megazineEdit", megazineService.getMegazine(megazineDTO.getMno()));
    return "megazine/megazineForm";
  }

  //수정 로직 : megazineService.getMegazine(megazineDTO.getMno())가 존재하면!!~~redirect해서 memberForm으로 보내주고
  // 보내준 경로에서 이미 값이 있으면 ~~ 값을 보여준 채로 label input 그대로 해서 수정할 수 있고 안의 값을 볼 수 있도록 하기=======================================================












  @GetMapping("/delete/{megazineId}")
  public String magazineDelete(@PathVariable("megazineId") Long megazineId, Model model){
    System.out.println("삭제합니다.====================" + megazineId);
    model.addAttribute("message",megazineId + "를 삭제하였습니다.");
    megazineService.delete(megazineId); //dto에서 mno(id)값 받아옴
    return "megazine/megazineMain";
  }


// 상세보기 관련 Url =================================================================================

  //User 매거진 상세보기 페이지
//  @GetMapping("/item/{megazineId}")
//  public String itemDtl(Model model, @PathVariable("megazineId") Long megazineId){
//
//    MegazineDTO megazineDTO = megazineService.getItemDtl(megazineId);
//    model.addAttribute("megazineDTO", megazineDTO);
//    return "megazine/megazineDetail";
//  }

  //상세보기 + 등록==============================>
  @GetMapping("/list/{megazineId}")
  public String megazineDtl(@PathVariable("megazineId") Long megazineId, Model model) {

    //안에 내용이 있으면 세부사항 보여주고
    //상세보기가 없다면 새로운 폼을 만들어서 준비시켜 -등록 
    try {
      MegazineDTO megazineDTO = megazineService.getItemDtl(megazineId);
      model.addAttribute("megazineDTO", megazineDTO);
    } catch (EntityNotFoundException e) {
      model.addAttribute("errorMessaage", "존재하지 않는 상품 입니다");
      model.addAttribute("megazineDTO", new MegazineDTO());
    }
    return "megazine/megazineUserDetail"; //버튼 있으면 저장 없으면 수정버튼이 보임 itemForm으로 똑같이 보내기로 함
  }


  //상세보기 유효성 검사는 수정할 때 하는건디?
  @PostMapping("/list/{megazineId}")
  public String itemUpdate(@Valid MegazineDTO megazineDTO,
                           BindingResult bindingResult,
                           @RequestParam("itemImgFile") MultipartFile itemImgFileList,
                           Model model) {

    //유효성 겁사에서 이슈 발생 시
    if (bindingResult.hasErrors()) {
      return "megazine/megazineMain"; //에러 나면 다시
    }
    //이미지가 비어 있으면
    if (itemImgFileList.isEmpty() && megazineDTO.getMno() == null) {
      model.addAttribute("errorMessage", "첫번재 상품 이미지는 필수입력입니다.");
      return  "megazine/megazineForm";
    }


    try {
      megazineService.updateItemImg(itemImgFileList,megazineDTO);

    } catch (Exception e){
      System.out.println(e.getMessage()); //에러 메시지 확인
      model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
      return "megazine/megazineUserDetail";///////
    }

    //수정 후 메인으로 감
    return "megazine/megazineMain";
  }




















}

