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

  public Member saveMember(Member member){
    validateMember(member);
    return memberRepository.save(member);
  }

  private void validateMember(Member member) {
    Member findMember = memberRepository.findByEmail(member.getEmail());
    if (findMember != null){
      throw new IllegalStateException("이미 가입된 회원입니다.");
    }
  }


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
