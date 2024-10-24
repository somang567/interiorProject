package com.keduit.interiors.dto;

import com.keduit.interiors.constant.ProductSell;
import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.entity.Product;
import com.keduit.interiors.entity.ProductImg;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@ToString
public class ProductDTO {
	private Long id;

	@NotBlank(message = "상품 이름 입력은 필수입니다.")
	private String productName;

	@NotBlank(message = "상품 상세설명은 필수입니다.")
	private String productDetail;

	@NotNull(message = "가격 입력은 필수입니다.")
	private int price;

	@NotNull(message = "재고 입력은 필수입니다.")
	private int stockNumber;

	@NotNull(message = "상품상태 설정은 필수입니다.")
	private ProductSell productSell;

	@NotNull(message = "상품 타입입력은 필수입니다.")
	private ProductType productType;

	private List<ProductImg> productImgList = new ArrayList<>();

	private ProductImgDTO fileUrl;

	private List<String> productImgUrls = new ArrayList<>();

	// 수정: List<MultipartFile> 필드의 이름을 HTML의 파일 필드 이름과 일치하게 유지합니다.
	private List<MultipartFile> multipartFileList;

	private String firstImageUrl; // 첫 번째 이미지 URL 필드 추가

	private static ModelMapper modelMapper = new ModelMapper();

	// BaseEntity에서 상속받는 필드 추가
	private String createBy; // 생성자
	private String modifyBy; // 수정자

	// BaseTimeEntity에서 상속받는 필드 추가
	private LocalDateTime regTime; // 생성 시간
	private LocalDateTime updateTime; // 수정 시간

	private Long memberId;
	// DTO를 엔티티로 변환하는 메서드
	public Product createProduct() {
		return modelMapper.map(this, Product.class);
	}

	// 엔티티를 DTO로 변환하는 메서드
	public static ProductDTO of(Product product) {
		return modelMapper.map(product, ProductDTO.class);
	}
}
