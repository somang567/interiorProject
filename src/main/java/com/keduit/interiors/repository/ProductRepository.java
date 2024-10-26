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
	// 상품명 검색
	List<Product> findByProductNameContainingAndProductType(String productName, ProductType productType);

	// 상품 내용 검색
	List<Product> findByProductDetailContainingAndProductType(String productDetail, ProductType productType);

	// 작성자 이메일로 검색
	@Query("SELECT p FROM Product p WHERE p.member.email LIKE %:email% AND p.productType = :productType")
	List<Product> findByMemberEmailContainingAndProductType(String email, ProductType productType);

}
