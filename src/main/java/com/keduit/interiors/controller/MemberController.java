////
//package com.keduit.interiors.controller;
//import com.keduit.interiors.dto.MemberDTO;
//import com.keduit.interiors.entity.Member;
//import com.keduit.interiors.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.security.crypto.password.PasswordEncoder;
////
//import javax.validation.Valid;
//import javax.validation.constraints.Size;
////
//@Controller
//@RequestMapping("/members")
//@RequiredArgsConstructor
//public class MemberController {
//
//  private final MemberService memberService;
//  private final PasswordEncoder passwordEncoder;
//
//  @GetMapping("/new")
//  public String memberForm(Model model){
//    System.out.println("--------boardForm-----------");
//    //생성한 MemberDTO 객체를 model에 "memberDTO"라는 이름으로 추가합니다. 이 모델은 뷰에서 사용할 수 있게 됩니다.
//    model.addAttribute("memberDTO", new MemberDTO()); //비어있는 상태 /폼에서 입력된 데이터를 받을 준비
//    return "member/memberForm"; //memberForm여기서
//  }
//
//  // memberDTO의 유효성 체크하기
//  // 사용자가 뷰에서 입력한 데이터 (memberDTO 데이터) 받아오는 곳
//  // @Valid를 사용하여 특정 객체의 필드에 적용된 제약 조건을 검증할 수 있습니다. 예를 들어, @NotNull, @Size, @Email 등과 같은 어노테이션을 함께 사용하여 각 필드의 유효성을 검사
//  @PostMapping("/new")
//  public String memberForm(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model){
//    if(bindingResult.hasErrors()){
//      return"member/memberForm";
//    }
//
//    try{
//      Member member = Member.createMember(memberDTO, passwordEncoder);
//      memberService.saveMember(member);
//    }catch(IllegalStateException e){
//      model.addAttribute("errorMessage",e.getMessage());
//      return"member/memberForm";
//    }
//
//    return"redirect:/";
//  }
//
//
//  @GetMapping("/login")
//  public String loginMember() {
//    return "member/memberForm";
//  }
//
//  @GetMapping("/login/error")
//  public String loginError(Model model) {
//    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
//    {
//      return "member/memberForm";
//    }
//  }
//}