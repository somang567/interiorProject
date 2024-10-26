package com.keduit.interiors.repository;

import com.keduit.interiors.constant.CsWriteType;
import com.keduit.interiors.entity.CSEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CsRepository extends JpaRepository<CSEntity, Long> {

	// 제목으로 검색
	@Query("SELECT c FROM CSEntity c WHERE c.title LIKE %:keyword% OR c.content LIKE %:keyword% OR c.member.name LIKE %:keyword%")
	Page<CSEntity> searchCs(@Param("keyword") String keyword, Pageable pageable);

	// NOTICE 게시물만 가져오기
	List<CSEntity> findByCsWriteTypeOrderByRegTimeDesc(CsWriteType csWriteType);

	// NOTICE가 아닌 일반 게시물만 가져오기 (페이지네이션 지원)
	Page<CSEntity> findByCsWriteTypeNotOrderByRegTimeDesc(CsWriteType csWriteType, Pageable pageable);

	Page<CSEntity> findByTitleContaining(String title, Pageable pageable);

	// 내용으로 검색
	Page<CSEntity> findByContentContaining(String content, Pageable pageable);

	// 작성자 이메일로 검색
	Page<CSEntity> findByMemberEmailContaining(String email, Pageable pageable);
}
