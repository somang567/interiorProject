package com.keduit.interiors.repository;

import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByProductType(ProductType productType);

	@Query("SELECT p FROM Product p ORDER BY function('RAND')")
	List<Product> findRandomProducts(Pageable pageable);
}
