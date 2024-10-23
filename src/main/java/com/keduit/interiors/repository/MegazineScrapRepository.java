package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.MegazineScrap;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MegazineScrapRepository extends JpaRepository<MegazineScrap, Long> {
    // 특정 사용자가 스크랩한 상품이 있는지 확인 (Member와 Product 객체 사용)
    boolean existsByMemberAndMegazine(Member member, Megazine megazine);

    // 특정 사용자가 스크랩한 상품 ID 목록 조회
    @Query("SELECT ps.megazine.mno FROM MegazineScrap ps WHERE ps.member.id = :memberId")
    List<Long> findMegazineMnosByMemberId(@Param("memberId") Long memberId);

    // 특정 사용자의 스크랩을 삭제 (Member와 Product 객체 사용)
    void deleteByMemberAndMegazine(Member member, Megazine megazine);
}