package com.keduit.interiors.controller;

import com.keduit.interiors.dto.MegazineScrapDTO;
import com.keduit.interiors.dto.ProductScrapDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.MemberRepository;
import com.keduit.interiors.service.MegazineScrapService;
import com.keduit.interiors.service.ProductScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/megascrap")
@RequiredArgsConstructor
public class MegazineScrapController {

    private final MegazineScrapService megazineScrapService;
    private final MemberRepository memberRepository;

    // 스크랩 추가
    @PostMapping("/add")
    public ResponseEntity<String> addScrap(Principal principal, @RequestBody MegazineScrapDTO megazineScrapDTO) {
        Long memberId = getMemberIdFromPrincipal(principal);
        megazineScrapService.addScrap(memberId, megazineScrapDTO.getMegazineId());
        return ResponseEntity.ok("스크랩이 추가되었습니다.");
    }

    // 스크랩 삭제
    @PostMapping("/remove")
    public ResponseEntity<String> removeScrap(Principal principal, @RequestBody MegazineScrapDTO megazineScrapDTO) {
        Long memberId = getMemberIdFromPrincipal(principal);
        megazineScrapService.removeScrap(memberId, megazineScrapDTO.getMegazineId());
        return ResponseEntity.ok("스크랩이 삭제되었습니다.");
    }

    // 특정 사용자가 스크랩한 상품 목록 반환
//    @GetMapping("/scrap-list")
//    public ResponseEntity<List<Long>> getScrapListForUser(Principal principal) {
//        Long memberId = getMemberIdFromPrincipal(principal);
//        //특정 사용자의 스크랩한 제품 ID 리스트를 가져옵니다.
//        List<Long> megazineScrapMegazineIds = megazineScrapService.getScrapMegazineIdsForUser(memberId);
//        return ResponseEntity.ok(megazineScrapMegazineIds);
//    }

    // Principal에서 memberId를 가져오는 로직
    private Long getMemberIdFromPrincipal(Principal principal) {
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new EntityNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email);
        }

        return member.getId();
    }
}