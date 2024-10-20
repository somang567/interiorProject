package com.keduit.interiors.dto;

import com.keduit.interiors.constant.CS;
import com.keduit.interiors.constant.CsWriteType;
import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.entity.CSEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@ToString
public class CsDTO {
	private Long id;
	private String title;
	private String content;

	private ProductType productType;
	private CsWriteType csWriteType;

	private CS csStatus;

	private Long memberId; // Member 객체가 아닌 memberId로 관리
	private String memberEmail; // 작성자의 이름 필드 추가


	// BaseEntity에서 상속받는 필드 추가
	private String createBy; // 생성자
	private String modifyBy; // 수정자

	// BaseTimeEntity에서 상속받는 필드 추가
	private LocalDateTime regTime; // 생성 시간
	private LocalDateTime updateTime; // 수정 시간

	private static ModelMapper modelMapper = new ModelMapper();

	// DTO를 엔티티로 변환하는 메서드
	public CSEntity createCS() {
		return modelMapper.map(this, CSEntity.class);
	}

	// 엔티티를 DTO로 변환하는 메서드
	public static CsDTO of(CSEntity cs) {
		CsDTO csDTO = modelMapper.map(cs, CsDTO.class);

		// member가 null인지 확인하고 안전하게 처리
		if (cs.getMember() != null) {
			csDTO.setMemberId(cs.getMember().getId()); // Member 객체에서 ID 추출
			csDTO.setMemberEmail(cs.getMember().getEmail()); // Member 객체에서 이름 추출
		} else {
			csDTO.setMemberId(null);  // member가 없는 경우 null로 설정
			csDTO.setMemberEmail("Anonymous");  // 이름이 없는 경우 기본값 설정
		}

		return csDTO;
	}
}
