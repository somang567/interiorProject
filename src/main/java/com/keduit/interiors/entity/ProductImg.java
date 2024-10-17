package com.keduit.interiors.entity;

import com.keduit.interiors.entity.BaseEntity;
import com.keduit.interiors.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class ProductImg extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_img_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product; // 이름 수정

	@Column(name = "origin_img_name")
	private String originImgName; // 카멜케이스로 수정

	@Column(name = "image_url")
	private String fileUrl; // 카멜케이스로 수정

	@Column(name = "img_name")
	private String imgName; // 카멜케이스로 수정

	private boolean isThumbnail;  // 썸네일 여부를 나타내는 필드 추가

	public void updateItemImg(String originImgName, String imgName, String fileUrl) {
		this.originImgName = originImgName;
		this.imgName = imgName;
		this.fileUrl = fileUrl;
	}
}
