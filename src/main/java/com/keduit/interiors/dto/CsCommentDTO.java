package com.keduit.interiors.dto;

import com.keduit.interiors.entity.CsComment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@ToString
public class CsCommentDTO {
	private Long id;
	private Long csId;
	private String content = "";
	private String memberEmail; // 댓글 작성자 이메일
	private LocalDateTime regTime;

	// Entity -> DTO 변환 메서드
	public static CsCommentDTO of(CsComment comment) {
		CsCommentDTO dto = new CsCommentDTO();
		dto.setId(comment.getId());
		dto.setCsId(comment.getCsEntity().getId());
		dto.setContent(comment.getContent());
		dto.setMemberEmail(comment.getMember().getEmail());
		dto.setRegTime(comment.getRegTime());
		return dto;
	}
}
