package com.keduit.interiors.repository;

import com.keduit.interiors.entity.SelfInteriorComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfInteriorCommentRepository extends JpaRepository<SelfInteriorComment, Long> {
    List<SelfInteriorComment> findBySelfInteriorId(Long selfInteriorId);
}
