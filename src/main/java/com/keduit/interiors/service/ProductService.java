package com.keduit.interiors.service;

import com.keduit.interiors.entity.Product;
import com.keduit.interiors.entity.ProductImg;
import com.keduit.interiors.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	// 상품 등록 메소드
	@Transactional
	public Product addProduct(Product product, List<ProductImg> productImages) {
		// 상품 저장
		Product savedProduct = productRepository.save(product);

		// 상품에 이미지 추가
		for (ProductImg img : productImages) {
			savedProduct.addProductImage(img); // Product에 ProductImg 추가
		}

		return savedProduct;
	}
}
