package com.keduit.interiors.controller;

import com.keduit.interiors.dto.ProductScrapDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.MemberRepository;
import com.keduit.interiors.service.ProductScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
public class ProductScrapController {

	private final ProductScrapService productScrapService;
	private final MemberRepository memberRepository;

	// 스크랩 추가
	@PostMapping("/add")
	public ResponseEntity<String> addScrap(Principal principal, @RequestBody ProductScrapDTO scrapDTO) {
		Long memberId = getMemberIdFromPrincipal(principal);
		productScrapService.addScrap(memberId, scrapDTO.getProductId());
		return ResponseEntity.ok("즐겨찾기가 등록되었습니다.");
	}

	// 스크랩 삭제
	@PostMapping("/remove")
	public ResponseEntity<String> removeScrap(Principal principal, @RequestBody ProductScrapDTO scrapDTO) {
		Long memberId = getMemberIdFromPrincipal(principal);
		productScrapService.removeScrap(memberId, scrapDTO.getProductId());
		return ResponseEntity.ok("스크랩이 삭제되었습니다.");
	}

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
