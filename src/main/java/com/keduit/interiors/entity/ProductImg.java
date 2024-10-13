package com.keduit.interiors.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class ProductImg extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_Img_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product_id;

	@Column(name = "origin_img_name")
	private String origin_img_name;

	@Column(name = "image_url")
	private String file_url;

	@Column(name = "img_name")
	private String img_name;

	public void updateItemImg(String origin_img_name, String img_name, String file_url){
		this.origin_img_name = origin_img_name;
		this.img_name = img_name;
		this.file_url = file_url;
	}
}
