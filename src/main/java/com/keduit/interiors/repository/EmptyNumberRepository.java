package com.keduit.interiors.repository;

import com.keduit.interiors.entity.EmptyNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmptyNumberRepository extends JpaRepository<EmptyNumber, Long> {
    Optional<EmptyNumber> findFirstByOrderByNumberAsc();
}
