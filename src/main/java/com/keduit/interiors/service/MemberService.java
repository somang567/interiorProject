package com.keduit.interiors.service;

import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
}
