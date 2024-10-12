package com.keduit.interiors.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Getter
@Setter
@ToString
public class ProductImgDTO {

	private Long id; // 이미지 ID (선택적)

	@NotBlank(message = "원본 이미지 이름은 필수입니다.")
	private String originImgName; // 원본 이미지 이름

	@NotBlank(message = "이미지 URL은 필수입니다.")
	private String fileUrl; // 이미지 URL

	@NotBlank(message = "이미지 이름은 필수입니다.")
	private String imgName; // 이미지 이름

	private List<MultipartFile> images; // 여러 개의 이미지 파일을 처리하기 위한 필드

}
