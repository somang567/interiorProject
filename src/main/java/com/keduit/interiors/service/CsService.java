package com.keduit.interiors.service;

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
			throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
		}

		csRepository.deleteById(id);  // 게시물 삭제
	}
}
