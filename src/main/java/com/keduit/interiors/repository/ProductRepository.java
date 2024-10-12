package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Member;
import com.keduit.interiors.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product , Long> {
	Product findProductById(Long product_id);

}
