package com.keduit.interiors.dto;

import com.keduit.interiors.constant.CS;
import com.keduit.interiors.constant.ProductSell;
import com.keduit.interiors.entity.Product;
import com.keduit.interiors.entity.ProductImg;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
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

	@NotBlank(message = "가격은 필수입력입니다.")
	private int price;

	private ProductSell productSell;

	private String productType;

	private List<ProductImg> productImgList = new ArrayList<>();
	private List<Long> ProductImgIds = new ArrayList<>();

	private static ModelMapper modelMapper = new ModelMapper();

	public Product createProductItem(){
		return modelMapper.map(this , Product.class);
	}

	public static ProductDTO of(Product product){
		return modelMapper.map(product , ProductDTO.class);
	}


}
