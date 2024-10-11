package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository : 데이터베이스와의 CRUD 작업을 수행하며, JPA와 같은 프레임워크를 사용하여 간단하게 구현 가능함.
public interface SearchRepository extends JpaRepository<Search, Long> {
}
