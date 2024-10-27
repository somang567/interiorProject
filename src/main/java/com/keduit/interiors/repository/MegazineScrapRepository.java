package com.keduit.interiors.repository;

import com.keduit.interiors.dto.MegazineDTO;
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
    
    //새로 추가한 친구 스크랩 매거진 이미지도 같이 가져오는 칭긔 MegazineDTO 타입 반환
    //@Query("SELECT new com.keduit.interiors.dto.MegazineDTO(m.mno, m.imageUrl) FROM Megazine m JOIN MegazineScrap s ON m.mno = s.megazine WHERE s.member = :memberId")
    //List<MegazineDTO> findScrapMegazinesByMemberId(@Param("memberId") Long memberId);

    //@Query("SELECT m FROM Megazine m JOIN MegazineScrap s ON m.mno = s.megazine WHERE s.member = :memberId")
    //List<Megazine> findScrapMegazinesByMemberId(@Param("memberId") Long memberId);

    //@Query("SELECT m FROM Megazine m JOIN MegazineScrap s ON m.mno = s.megazine.mno WHERE s.member = :memberId")
    //List<Megazine> findScrapMegazinesByMemberId(@Param("memberId") Long memberId);

    //새로 추가한 칭긔
    //사용자가 스크랩한 매거진을 가져오는 쿼리 메서드를 추가합니다.

    @Query("SELECT s.megazine FROM MegazineScrap s WHERE s.member.id = :memberId")
    List<Megazine> findScrapMegazinesByMemberId(@Param("memberId") Long memberId);

}