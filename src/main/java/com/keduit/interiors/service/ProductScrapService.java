package com.keduit.interiors.service;

import com.keduit.interiors.entity.Member;
import com.keduit.interiors.entity.Product;
import com.keduit.interiors.entity.ProductScrap;
import com.keduit.interiors.repository.MemberRepository;
import com.keduit.interiors.repository.ProductRepository;
import com.keduit.interiors.repository.ProductScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductScrapService {

	private final ProductScrapRepository productScrapRepository;
	private final MemberRepository memberRepository;  // Member 엔티티를 찾기 위한 레포지토리
	private final ProductRepository productRepository; // Product 엔티티를 찾기 위한 레포지토리

	// 스크랩 추가
	public void addScrap(Long memberId, Long productId) {
		// memberId와 productId를 기반으로 각각 Member와 Product 엔티티 조회
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다. ID: " + memberId));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다. ID: " + productId));

		// 이미 스크랩이 존재하는지 확인
		if (productScrapRepository.existsByMemberAndProduct(member, product)) {
			throw new IllegalStateException("이미 스크랩된 상품입니다.");
		}

		// ProductScrap 엔티티 생성 및 저장
		ProductScrap scrap = new ProductScrap();
		scrap.setMember(member);  // Member 엔티티 설정
		scrap.setProduct(product);  // Product 엔티티 설정
		productScrapRepository.save(scrap);
	}

	// 스크랩 삭제
	public void removeScrap(Long memberId, Long productId) {
		// memberId와 productId를 기반으로 각각 Member와 Product 엔티티 조회
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다. ID: " + memberId));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다. ID: " + productId));

		// ProductScrap 엔티티 삭제
		productScrapRepository.deleteByMemberAndProduct(member, product);
	}


	// 특정 사용자가 스크랩한 상품 목록 반환
	public List<Long> getScrapProductIdsForUser(Long memberId) {
		// memberId를 기반으로 스크랩된 상품 ID 목록 반환
		return productScrapRepository.findProductIdsByMemberId(memberId);
	}
}
