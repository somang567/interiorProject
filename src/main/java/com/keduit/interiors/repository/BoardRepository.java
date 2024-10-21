package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContaining(String title, Pageable pageable);

    Page<Board> findByContentContaining(String content, Pageable pageable);

    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}
