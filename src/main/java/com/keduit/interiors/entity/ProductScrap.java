//package com.keduit.interiors.entity;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//@Setter
//@ToString
//public class ProductScrap extends BaseEntity{
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "productScrapId")
//	private Long id;
//
//	@ManyToOne
//	@JoinColumn(name = "member_id")
//	private Member member;
//
//	@ManyToOne
//	@JoinColumn(name = "product_id")
//	private Product product;
//
//
//}
