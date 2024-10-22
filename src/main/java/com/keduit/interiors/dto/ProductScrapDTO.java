package com.keduit.interiors.dto;

import com.keduit.interiors.entity.ProductScrap;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString

public class ProductScrapDTO {
	private Long id;
	private Long memberId;  // 엔티티 대신 ID 사용
	private Long productId; // 엔티티 대신 ID 사용

}
