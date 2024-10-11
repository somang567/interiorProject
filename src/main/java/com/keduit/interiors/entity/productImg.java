package com.keduit.interiors.entity;

import com.keduit.interiors.entity.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class productImg extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_Img_id")
	private Long id;

	@JoinColumn(name = "product_id")
	private product product_id;

	@Column(name = "origin_img_name")
	private String origin_img_name;

	@Column(name = "image_url")
	private String file_url;

	@Column(name = "img_name")
	private String img_name;
}
