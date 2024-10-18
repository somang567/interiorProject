package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Product;
import com.keduit.interiors.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {
	// product 필드로 상품 이미지를 찾음 (연관관계에 맞게 수정)
	List<ProductImg> findByProductIdOrderByIdAsc(Long id);
}
