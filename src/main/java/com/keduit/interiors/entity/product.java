//package com.keduit.interiors.entity;
//
//import com.keduit.interiors.constant.CS;
//import com.keduit.interiors.dto.productDTO;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.modelmapper.ModelMapper;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//@Setter
//@ToString
//public class product extends BaseEntity{
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "product_id")
//	private Long id;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "member_id")
//	private Member member;
//
//	@Lob
//	@Column(nullable = false , length = 50)
//	private String product_name;
//
//	@Lob
//	@Column(nullable = false)
//	private String product_Detail;     // 상품 상세설명
//
//	private int price;
//
//	private CS csStatus;
//
//	private static ModelMapper modelMapper = new ModelMapper();
//
//	private void updateItem(productDTO product){
//		this.product_name = product.getProductName();
//		this.product_Detail = product.getProductDetail();
//		this.price = product.getPrice();
//		this.csStatus = product.getCsStatus();
//	}
//
//}
