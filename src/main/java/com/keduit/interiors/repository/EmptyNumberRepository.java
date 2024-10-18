package com.keduit.interiors.repository;

import com.keduit.interiors.entity.EmptyNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmptyNumberRepository extends JpaRepository<EmptyNumber, Long> {

    // 가장 작은 번호를 가진 EmptyNumber 엔티티를 조회
    Optional<EmptyNumber> findFirstByOrderByNumberAsc();

}
