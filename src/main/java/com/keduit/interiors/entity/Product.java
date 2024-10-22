package com.keduit.interiors.entity;

import com.keduit.interiors.constant.ProductSell;
import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.BaseEntity;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.entity.ProductImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
	@Column(name = "productId")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;

	@Lob
	@Column(name = "productName", nullable = false , length = 50)
	private String productName;

	@Lob
	@Column(name = "productDetail", nullable = false)
	private String productDetail;

	@Column(name = "price")
	private int price;

	@Column(name = "stockNumber")
	private int stockNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "productSell")
	private ProductSell productSell;

	// ProductType enum으로 수정
	@Enumerated(EnumType.STRING)
	@Column(name = "productType")
	private ProductType productType;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductImg> productImgList = new ArrayList<>();


//	@OneToMany(mappedBy = "product" , cascade = CascadeType.REMOVE)
//	private List<ProductScrap> scrapList = new ArrayList<>();
//

	// 상품 이미지 추가 메소드
	public void addProductImage(ProductImg productImg) {
		productImgList.add(productImg);
		productImg.setProduct(this);
	}

	public void updateItem(ProductDTO product) {
		this.productName = product.getProductName();
		this.productDetail = product.getProductDetail();
		this.price = product.getPrice();
		this.stockNumber = product.getStockNumber();
		this.productSell = product.getProductSell();
		this.productType = product.getProductType();
	}
}
