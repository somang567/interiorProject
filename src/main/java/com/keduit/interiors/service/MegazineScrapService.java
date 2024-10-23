package com.keduit.interiors.service;

import com.keduit.interiors.dto.MegazineDTO;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.MegazineScrap;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.MegazineRepository;
import com.keduit.interiors.repository.MegazineScrapRepository;
import com.keduit.interiors.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MegazineScrapService {

    private final MegazineScrapRepository megazineScrapRepository;
    private final MemberRepository memberRepository;  // Member 엔티티를 찾기 위한 레포지토리
    private final MegazineRepository megazineRepository;

    // 스크랩 추가
    public void addScrap(Long memberId, Long megazineId) {
        // memberId와 productId를 기반으로 각각 Member와 Product 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다. ID: " + memberId));
        Megazine megazine = megazineRepository.findById(megazineId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다. ID: " + megazineId));

        // 이미 스크랩이 존재하는지 확인
        if (megazineScrapRepository.existsByMemberAndMegazine(member, megazine)) {
            throw new IllegalStateException("이미 스크랩된 상품입니다.");
        }

        // ProductScrap 엔티티 생성 및 저장
        MegazineScrap scrap = new MegazineScrap();
        scrap.setMember(member);  // Member 엔티티 설정
        scrap.setMegazine(megazine);  // Product 엔티티 설정
        megazineScrapRepository.save(scrap);
    }

    // 스크랩 삭제
    public void removeScrap(Long memberId, Long megazineId) {
        // memberId와 productId를 기반으로 각각 Member와 Product 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다. ID: " + memberId));
        Megazine megazine = megazineRepository.findById(megazineId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다. ID: " + megazineId));

        // ProductScrap 엔티티 삭제
        megazineScrapRepository.deleteByMemberAndMegazine(member, megazine);
    }

    // 특정 사용자가 스크랩한 상품 목록 반환
    public List<Long> getScrapMegazineIdsForUser(Long memberId) {
        // memberId를 기반으로 스크랩된 상품 ID 목록 반환
        return megazineScrapRepository.findMegazineMnosByMemberId(memberId);
    }


    //서버에서 메거진 자체를
    public List<Megazine> getScrapMegazinesForUser(Long memberId) {
        return megazineScrapRepository.findScrapMegazinesByMemberId(memberId);
    }

    //새로 추가한 칭긔
    @Transactional(readOnly = true)
    public List<Megazine> getScrappedMegazines(Long memberId) {
        return megazineScrapRepository.findScrapMegazinesByMemberId(memberId);
    }

}