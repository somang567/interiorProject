package com.keduit.interiors.entity;

import com.keduit.interiors.constant.CS;
import com.keduit.interiors.constant.CsWriteType;
import com.keduit.interiors.constant.ProductType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class CSEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Lob
	private String content;

	@Enumerated(EnumType.STRING)
	private ProductType productType;

	@Enumerated(EnumType.STRING)
	private CsWriteType csWriteType;

	@Enumerated(EnumType.STRING)
	private CS csStatus;

	// 문의글 작성자 (회원)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member member;


	// 문의글 업데이트 메서드
	public void updateCs(String title, String content, ProductType productType, CsWriteType csWriteType, com.keduit.interiors.constant.CS csStatus) {
		this.title = title;
		this.content = content;
		this.productType = productType;
		this.csWriteType = csWriteType;
		this.csStatus = csStatus;
	}
}
