package com.keduit.interiors.dto;

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
	private String content;
	private String memberEmail; // 댓글 작성자 이메일
	private LocalDateTime regTime;
}
