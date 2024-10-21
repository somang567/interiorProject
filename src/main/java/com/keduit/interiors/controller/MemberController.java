////
package com.keduit.interiors.controller;
//
import com.keduit.interiors.dto.MemberDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

////
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/new")
  public String memberForm(Model model) {
    System.out.println("--------boardForm-----------");
    model.addAttribute("memberDTO", new MemberDTO()); //비어있는 상태 /폼에서 입력된 데이터를 받을 준비
    return "member/memberRegisterForm"; //memberForm여기서
  }

  // memberDTO의 유효성 체크하기
  // 사용자가 뷰에서 입력한 데이터 (memberDTO 데이터) 받아오는 곳
  // @Valid를 사용하여 특정 객체의 필드에 적용된 제약 조건을 검증할 수 있습니다. 예를 들어, @NotNull, @Size, @Email 등과 같은 어노테이션을 함께 사용하여 각 필드의 유효성을 검사
  @PostMapping("/new")
  public String memberForm(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors()) {
      return "member/memberRegisterForm";
    }

    try {
      Member member = Member.createMember(memberDTO, passwordEncoder);  //memberDtO를 member로 바꿔줌
      memberService.saveMember(member);
    } catch (IllegalStateException e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "member/memberRegisterForm";
    }

    return "redirect:/";  //회원가입 완료 후 메인 페이지로 이동
  }

  // 정보 수정 폼을 보여주는 GET 매핑
  @GetMapping("/edit")
  public String editForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    String email = userDetails.getUsername();
    Member member = memberService.findByEmail(email);

    if (member == null) {
      model.addAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
      return "member/memberForm";
    }

    MemberDTO memberDTO = MemberDTO.builder()
        .name(member.getName())
        .email(member.getEmail())
        .address(member.getAddress())
        .role(member.getRole().toString())
        .build();

    model.addAttribute("memberDTO", memberDTO);
    return "member/edit";
  }

  // 정보 수정 처리하는 POST 매핑
  @PostMapping("/edit")
  public String edit(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model, @AuthenticationPrincipal UserDetails userDetails) {

    if (bindingResult.hasErrors()) {
      return "member/edit";
    }

    try {
      memberService.updateMember(userDetails.getUsername(), memberDTO, passwordEncoder);
    } catch (IllegalStateException e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "member/edit";
    }

    return "redirect:/";
  }

  @GetMapping("/login")
  public String loginMember() {
    return "member/memberForm";
  }

  @GetMapping("/login/error")
  public String loginError(Model model) {
    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");

    return "member/memberForm";

  }

  // 회원 탈퇴 처리 메서드
  @PostMapping("/withdraw")
  @ResponseBody
  public String withdraw(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
    String email = userDetails.getUsername();
    try {
      memberService.deleteMember(email);
      // 세션 무효화
      session.invalidate();
      return "success";
    } catch (Exception e) {
      return "fail";
    }
  }

}