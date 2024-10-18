package com.keduit.interiors.repository;

import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByProductType(ProductType productType);
	Product findProductById(Long productId);
}
