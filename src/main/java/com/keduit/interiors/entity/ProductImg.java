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
	@Column(name = "productImgId")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	private Product product; // 이름 수정

	@Column(name = "originImgName")
	private String originImgName; // 카멜케이스로 수정

	@Column(name = "imageUrl")
	private String fileUrl; // 카멜케이스로 수정

	@Column(name = "imgName")
	private String imgName; // 카멜케이스로 수정

	private boolean isThumbnail;  // 썸네일 여부를 나타내는 필드 추가

	public void updateItemImg(String originImgName, String imgName, String fileUrl) {
		this.originImgName = originImgName;
		this.imgName = imgName;
		this.fileUrl = fileUrl;
	}
}
