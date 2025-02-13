package com.keduit.interiors.service;

import com.keduit.interiors.constant.CS;
import com.keduit.interiors.dto.CsDTO;
import com.keduit.interiors.entity.CSEntity;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.CsRepository;
import com.keduit.interiors.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CsService {

	private final CsRepository csRepository;
	private final MemberRepository memberRepository;

	// CS 등록
	public CSEntity saveCs(CsDTO csDTO) {
		Member member = memberRepository.findById(csDTO.getMemberId())
				.orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + csDTO.getMemberId()));

		CSEntity csEntity = csDTO.createCS();
		csEntity.setMember(member);

		return csRepository.save(csEntity);
	}

	// 페이지네이션 적용한 CS 목록 전체 조회
	public Page<CsDTO> getAllCs(Pageable pageable) {
		return csRepository.findAll(pageable)
				.map(CsDTO::of);  // CSEntity를 CsDTO로 변환
	}

	// CS 단건 조회 (상세보기)
	public CsDTO getCsById(Long id) {
		CSEntity csEntity = csRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 CS를 찾을 수 없습니다. ID: " + id));

		return CsDTO.of(csEntity);  // CSEntity를 CsDTO로 변환
	}

	// CS 수정
	public void updateCs(Long id, CsDTO csDTO, String email) {
		CSEntity csEntity = csRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 CS를 찾을 수 없습니다. ID: " + id));

		// 작성자 확인 (이메일 비교)
		if (!csEntity.getMember().getEmail().equals(email)) {
			throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
		}

		csEntity.setTitle(csDTO.getTitle());
		csEntity.setContent(csDTO.getContent());
		csEntity.setProductType(csDTO.getProductType());
		csEntity.setCsWriteType(csDTO.getCsWriteType());
		csEntity.setCsStatus(csDTO.getCsStatus());

		csRepository.save(csEntity);  // 수정된 내용 저장
	}

	// CS 삭제
	public void deleteCs(Long id, String email) {
		CSEntity csEntity = csRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 CS를 찾을 수 없습니다. ID: " + id));

		// 작성자 확인 (이메일 비교)
		if (!csEntity.getMember().getEmail().equals(email)) {
			// 관리자도 삭제 가능하도록 추가
			Member member = memberRepository.findByEmail(email);
			if (!member.getRole().equals("ROLE_ADMIN") && member.getRole().equals("ROLE_USER")) {
				throw new IllegalArgumentException("작성자 또는 관리자만 삭제할 수 있습니다.");
			}
		}
		csRepository.deleteById(id);
	}

	// 검색
	public Page<CsDTO> searchCs(String category, String keyword, Pageable pageable) {
		Page<CSEntity> csPage;
		switch (category) {
			case "title":
				csPage = csRepository.findByTitleContaining(keyword, pageable);
				break;
			case "content":
				csPage = csRepository.findByContentContaining(keyword, pageable);
				break;
			case "member.email":
				csPage = csRepository.findByMemberEmailContaining(keyword, pageable);
				break;
			default:
				throw new IllegalArgumentException("잘못된 검색 카테고리입니다: " + category);
		}
		return csPage.map(CsDTO::of);
	}

	// CS 상태 업데이트
	public void updateCsStatus(Long csId, CS status) {
		CSEntity csEntity = csRepository.findById(csId)
				.orElseThrow(() -> new IllegalArgumentException("해당 CS를 찾을 수 없습니다. ID: " + csId));

		csEntity.setCsStatus(status);  // 엔티티의 상태 업데이트
		csRepository.save(csEntity);  // 변경 사항 저장
	}
}
