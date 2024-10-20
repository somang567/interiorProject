package com.keduit.interiors.repository;

import com.keduit.interiors.entity.CSEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CsRepository extends JpaRepository<CSEntity, Long> {

	// 제목으로 검색
	@Query("SELECT c FROM CSEntity c WHERE c.title LIKE %:keyword% OR c.content LIKE %:keyword% OR c.member.name LIKE %:keyword%")
	Page<CSEntity> searchCs(@Param("keyword") String keyword, Pageable pageable);
}
