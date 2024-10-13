package com.keduit.interiors.dto;

import com.keduit.interiors.entity.ProductImg;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
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
	private String origin_Img_Name; // 원본 이미지 이름

	@NotBlank(message = "이미지 URL은 필수입니다.")
	private String file_Url; // 이미지 URL

	@NotBlank(message = "이미지 이름은 필수입니다.")
	private String img_name; // 이미지 이름

	private static ModelMapper modelMapper = new ModelMapper();
	public static ProductImgDTO of(ProductImg productImg){
		return modelMapper.map(productImg , ProductImgDTO.class);
	}
}
