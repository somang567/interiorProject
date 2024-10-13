package com.keduit.interiors.service;

import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.Product;
import com.keduit.interiors.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public void addProduct(ProductDTO productDTO) {
		Product product = productDTO.createProductItem(); // DTO를 엔티티로 변환
		productRepository.save(product); // DB에 저장
	}

}
