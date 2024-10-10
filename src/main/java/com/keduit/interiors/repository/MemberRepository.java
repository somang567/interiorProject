package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);
}
