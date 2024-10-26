package com.keduit.interiors.repository;

import com.keduit.interiors.entity.SelfInterior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface SelfInteriorRepository extends JpaRepository<SelfInterior, Long> {
    Page<SelfInterior> findByTitleContaining(String title, Pageable pageable);

    Page<SelfInterior> findByContentContaining(String content, Pageable pageable);

    Page<SelfInterior> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}
