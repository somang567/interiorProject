package com.keduit.interiors.dto;

import com.keduit.interiors.constant.CS;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@ToString
public class productDTO {
	private Long id;

	@NotBlank(message = "상품 이름 입력은 필수입니다.")
	private String productName;

	@NotBlank(message = "상품 상세설명은 필수입니다.")
	private String productDetail;

	@NotBlank(message = "가격은 필수입력입니다.")
	private int price;

	private CS csStatus;

	private static ModelMapper modelMapper = new ModelMapper();


}
