package com.keduit.interiors.repository;

import com.keduit.interiors.entity.CsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CsCommentRepository extends JpaRepository<CsComment , Long> {
	List<CsComment> findByCsEntityId(Long csId);
}
