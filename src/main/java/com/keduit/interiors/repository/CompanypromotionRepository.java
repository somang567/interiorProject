package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Companypromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanypromotionRepository extends JpaRepository<Companypromotion, Long> {
    Page<Companypromotion> findByTitleContaining(String title, Pageable pageable);
    Page<Companypromotion> findByContentContaining(String content, Pageable pageable);
    Page<Companypromotion> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}


