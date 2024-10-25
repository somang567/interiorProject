package com.keduit.interiors.controller;

import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.dto.*;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.MegazineRepository;
import com.keduit.interiors.service.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/megazines")
@RequiredArgsConstructor
public class MegazineController {

  //private final Megazine megazine;
  private final MegazineService megazineService;
  private final MemberService memberService;
  private final MegazineRepository megazineRepository;
  private final MegazineCommentService megazineCommentService;
  private final MegazineCommentServiceImpl megazineCommentServiceImpl;
  private final MegazineScrapService megazineScrapService;

  //  String searchKeyword,
  @GetMapping("/list")
  public String megazineItem(
          @RequestParam(required = false) String searchKeyword,
          @PageableDefault(page = 0, size = 4, sort = "mno", direction = Sort.Direction.DESC) Pageable pageable,
          Model model, Principal principal) {

    List<MegazineDTO> megazineProducts = megazineService.getMegazineList();
    model.addAttribute("megazineList", megazineProducts);
    if (principal != null) {
      String username = principal.getName();
      Member member = memberService.findByEmail(username);
      model.addAttribute("authorName", member.getName()); //이거 주석처리하니까 됨
    }

    String memberId = principal.getName();
    if(memberId == null){
      System.out.println("아직 로그인한 사용자가 없음요---------");
    }else{
      memberId = principal.getName();
    }
    // 또는 사용자 ID가 필요하다면, UserDetailsService를 사용하여 사용자 정보를 가져올 수 있습니다.
    Member userDetails = memberService.findByEmail(memberId);
    Long userId = userDetails.getId(); // 사용자 ID를 가져오는 예 (UserDetails 구현에 따라 다를 수 있음)
    model.addAttribute("scrapList", megazineScrapService.getScrapMegazineIdsForUser(userId));


    Page<Megazine> list = null;

    if (searchKeyword == null) {
      list = megazineService.getListItemPage(pageable); // 메인페이지 리스트 부분
      model.addAttribute("list", list);
    } else {
      list = megazineService.megazineSearchList(searchKeyword, pageable); // 메인페이지 리스트 부분
      model.addAttribute("list", list);
    }

    int nowPage = list.getPageable().getPageNumber() + 1; //pageable에서 넘어온 현재 페이지를 반환/ 페이지 1부터 시작.
    int startPage = Math.max(nowPage - 4, 1);
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
  public String megazineNew(Model model) {
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
  public String itemDtl(Model model, @PathVariable("megazineId") Long megazineId, Principal principal) {

    MegazineDTO megazineDTO = megazineService.getItemDtl(megazineId);
    model.addAttribute("megazineDTO", megazineDTO);

    //댓글 관련 로직getCommentsByBoardId(id);
    List<MegazineCommentDTO> comments = megazineCommentServiceImpl.getCommentsByMegazineId(megazineId);
    Long currentUserId = null;
    if (principal != null) {
      Member member = memberService.findByEmail(principal.getName());
      currentUserId = member.getId();
    }

    if (comments == null) {
      //model.addAttribute("errorMessage", "해당 댓글이 존재하지 않습니다.");
    }else{
      for (MegazineCommentDTO comment : comments) {
        comment.setEditable(currentUserId != null && currentUserId.equals(comment.getAuthorId()));
      }
      model.addAttribute("comments", comments);
    }

    long totalCnt = megazineService.countTotalComments();  //전체 매거진 개수
    model.addAttribute("totalCnt", totalCnt);
  //댓글 관련 로직 End
    return "megazine/megazineDetail";
  }

  //사용자 수정
  @GetMapping("/edit/{megazineId}")
  public String itemEdit(Model model, @PathVariable("megazineId") Long megazineId) {

    MegazineDTO megazineDTO = megazineService.getItemDtl(megazineId);
    if (megazineDTO == null) {
      System.out.println("megazineDTO is null!");
    }
    model.addAttribute("megazineDTO", megazineDTO);
    return "megazine/megazineEdit";
  }


  @PostMapping("/edit/{megazineId}")
  public String itemEdited(@Valid MegazineDTO megazineDTO, BindingResult bindingResult,
                           Model model, @PathVariable("megazineId") Long megazineId,
                           @RequestParam("itemImgFile") MultipartFile itemImgFile) throws Exception {

    //megazineService.updateItemImg(itemImgFile,megazineDTO);
    megazineDTO = megazineService.getItemDtl(megazineId); //이미 있는 것을 읽어옴


    //내용이 없으면 지 알아서 form으로 넘어감
    megazineDTO.getTitle();
    megazineDTO.getContent();
    megazineDTO.getOriImgName();

    megazineDTO.setTitle(megazineDTO.getTitle());
    megazineDTO.setContent(megazineDTO.getContent());
    megazineDTO.setOriImgName(megazineDTO.getOriImgName());

    try {
      megazineService.saveItem(megazineDTO, itemImgFile);

    } catch (Exception e) {
      model.addAttribute("errorMessage", "매거진 등록 중 에러가 발생하였습니다.");
      System.out.println("====================================");
      e.printStackTrace();
      return "megazine/megazineEdit";
    }
    model.addAttribute("megazineDTO", megazineDTO);
    return "redirect:/megazines/list";
  }


  //삭제
  // 게시글 삭제 처리
  @GetMapping("/delete/{megazineId}")
  public String boardDelete(@PathVariable Long megazineId) {
    megazineService.delete(megazineId);
    return "redirect:/megazines/list";
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
      return "megazine/megazineDetail";
    }


    try {
      megazineService.updateItemImg(itemImgFileList, megazineDTO);
    } catch (Exception e) {
      System.out.println(e.getMessage()); //에러 메시지 확인
      model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
      return "megazine/megazineForm";
    }

    //수정 후 메인으로 감
    return "redirect:/megazines/list";
  }

//  @PostMapping("/submitForm") // 실제 폼의 action URL과 일치해야 함
//  public String handleSubmit(@ModelAttribute MegazineDTO megazineDTO) {
//    // 폼 처리 로직
//    return "redirect:/successPage"; // 성공 시 리다이렉트
//  }

  // 댓글 추가 처리 (AJAX 요청)------------------------------------------------
  @PostMapping("/{megazineId}/comment")
  @ResponseBody
  public ResponseEntity<?> addComment(@PathVariable("megazineId") Long megazineId,
                                      @RequestBody Map<String, String> payload,
                                      Principal principal) {
    System.out.println("megazineId-============================="+ megazineId);
    System.out.println("댓글 Post 매핑 여기로 넘어옴-=============================");

    if (principal == null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "로그인이 필요합니다.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    try {
      String content = payload.get("content");
      if (content == null || content.trim().isEmpty()) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "댓글 내용을 입력하세요.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
      }


      MegazineCommentDTO megazineCommentDTO = new MegazineCommentDTO();
      megazineCommentDTO.setMegazineId(megazineId);
      megazineCommentDTO.setContent(content);

      Member member = memberService.findByEmail(principal.getName());
      MegazineCommentDTO savedMegazineCommentDTO = megazineCommentService.saveComment(megazineCommentDTO, member);


      // 댓글이 제대로 저장된 경우 반환
      // 서버에서 생성된 댓글의 데이터 전송 객체(Data Transfer Object)입니다
      return ResponseEntity.ok(savedMegazineCommentDTO);
    } catch (Exception e) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "댓글 추가 중 오류가 발생했습니다: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }

  // 댓글 수정 처리 (AJAX 요청)
  @PostMapping("/comment/update/{megazineId}")
  @ResponseBody
  public ResponseEntity<?> updateComment(@PathVariable("megazineId") Long id,
                                         @RequestBody Map<String, String> payload,
                                         Principal principal) {
    if (!hasPermissionToModifyOrDeleteComment(id, principal)) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "권한이 없습니다. 댓글을 수정할 수 없습니다.");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    try {
      String content = payload.get("content");
      if (content == null || content.trim().isEmpty()) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "댓글 내용을 입력하세요.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
      }

      MegazineCommentDTO megazineCommentDTO = megazineCommentServiceImpl.getCommentById(id);
      megazineCommentDTO.setContent(content);

      Member member = memberService.findByEmail(principal.getName());
      megazineCommentServiceImpl.updateComment(megazineCommentDTO, member);

      return ResponseEntity.ok(megazineCommentDTO);
    } catch (Exception e) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "댓글 수정 중 오류가 발생했습니다: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }

  // 권한 확인 메서드 (댓글)
  private boolean hasPermissionToModifyOrDeleteComment(Long commentId, Principal principal) {
    if (principal == null) {
      return false;
    }
    MegazineCommentDTO megazineCommentDTO = megazineCommentServiceImpl.getCommentById(commentId);
    if (megazineCommentDTO == null) {
      return false;
    }
    Member member = memberService.findByEmail(principal.getName());
    return megazineCommentDTO.getAuthorId().equals(member.getId()) || "ROLE_ADMIN".equals(member.getRole());
  }

  // 권한 확인 메서드 (매거진)
  private boolean hasPermissionToModifyOrDeleteMegazine(Long megazineId, Principal principal) {
    if (principal == null) {
      return false;
    }
    MegazineDTO megazineDTO = megazineService.megazineView(megazineId);
    if (megazineDTO == null) {
      return false;
    }
    Member member = memberService.findByEmail(principal.getName());
    return megazineDTO.getUser().equals(member.getId()) || "ROLE_ADMIN".equals(member.getRole());
  }


  // 댓글 삭제 처리 (AJAX 요청)
  @DeleteMapping("/comment/delete/{megazineId}")
  @ResponseBody
  public ResponseEntity<?> deleteComment(@PathVariable("megazineId") Long id, Principal principal) {
    if (!hasPermissionToModifyOrDeleteComment(id, principal)) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "권한이 없습니다. 댓글을 삭제할 수 없습니다.");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    try {
      megazineCommentServiceImpl.deleteComment(id, memberService.findByEmail(principal.getName()));
      Map<String, String> successResponse = new HashMap<>();
      successResponse.put("message", "댓글이 성공적으로 삭제되었습니다.");
      return ResponseEntity.ok(successResponse);
    } catch (Exception e) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "댓글 삭제 중 오류가 발생했습니다: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }
}