package com.keduit.interiors.entity;

import com.keduit.interiors.constant.ProductSell;
import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.BaseEntity;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.entity.ProductImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Lob
	@Column(name = "product_name", nullable = false , length = 50)
	private String product_name;

	@Lob
	@Column(name = "product_Detail", nullable = false)
	private String product_Detail;     // 상품 상세설명

	@Column(name = "price")
	private int price;

	@Column(name = "product_sell")
	private ProductSell productSell;

	// 추가: 상품 종류 필드
	@Column(name = "product_type")
	private String productType;

	@OneToMany(mappedBy = "product_id", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductImg> productImgList = new ArrayList<>();

	// 상품 이미지 추가 메소드
	public void addProductImage(ProductImg productImg) {
		productImgList.add(productImg);
		productImg.setProduct_id(this); // 양방향 관계 설정
	}

	public void updateItem(ProductDTO product) {
		this.product_name = product.getProductName();
		this.product_Detail = product.getProductDetail();
		this.price = product.getPrice();
		this.productSell = product.getProductSell();
		this.productType = product.getProductType(); // 추가된 필드 업데이트
	}
}
