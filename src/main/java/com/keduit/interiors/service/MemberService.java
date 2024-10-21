package com.keduit.interiors.service;

import com.keduit.interiors.dto.MemberDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;

  // 회원 저장 메서드
  public Member saveMember(Member member){
    validateMember(member);
    return memberRepository.save(member);
  }

  // 회원 중복 확인 메서드
  private void validateMember(Member member) {
    Member findMember = memberRepository.findByEmail(member.getEmail());
    if (findMember != null){
      throw new IllegalStateException("이미 가입된 회원입니다.");
    }
  }

  // 이메일로 회원 조회 메서드 추가
  public Member findByEmail(String email) {
    return memberRepository.findByEmail(email);
  }

  // UserDetailsService 구현 메서드
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email);
    System.out.println("------- member => " + member);
    if (member == null){
      throw new UsernameNotFoundException(email);
    }
    return User.builder()
            .username(member.getEmail())
            .password(member.getPassword())
            .roles(member.getRole().toString())
            .build();
  }

  // 회원 정보 수정 메서드
  public void updateMember(String email, MemberDTO memberDTO, PasswordEncoder passwordEncoder) {
    Member member = memberRepository.findByEmail(email);
    if (member == null) {
      throw new IllegalStateException("수정할 회원을 찾을 수 없습니다.");
    }

    member.setName(memberDTO.getName());
    member.setAddress(memberDTO.getAddress());

    // 비밀번호 변경을 원할 경우
    if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
      member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
    }

    // 역할(Role)은 일반적으로 변경하지 않으므로 생략하거나 별도의 관리 로직을 추가
    // member.setRole(memberDTO.getRole());

    memberRepository.save(member); // 변경 감지를 사용한다면 필요 없을 수 있음
  }
}
