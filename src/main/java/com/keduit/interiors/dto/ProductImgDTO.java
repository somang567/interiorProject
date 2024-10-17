package com.keduit.interiors.dto;

import com.keduit.interiors.entity.ProductImg;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductImgDTO {

	private Long id; // 이미지 ID

	private String originImgName; // 원본 이미지 이름

	private String fileUrl; // 이미지 URL

	private String imgName; // 이미지 이름

	private MultipartFile file; // 실제 파일 업로드용 필드

	private boolean isThumbnail; // 상품 썸네일 나타내기 위한 필드변수

	private static ModelMapper modelMapper = new ModelMapper();

	// ProductImg 엔티티를 ProductImgDTO로 변환하는 메서드
	public static ProductImgDTO of(ProductImg productImg) {
		return modelMapper.map(productImg, ProductImgDTO.class);
	}

	// ProductImgDTO를 ProductImg 엔티티로 변환하는 메서드
	public ProductImg toEntity() {
		return modelMapper.map(this, ProductImg.class);
	}
}
