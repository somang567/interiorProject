package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 제목에 특정 문자열을 포함하는 게시글을 페이징하여 조회
    Page<Board> findByTitleContaining(String title, Pageable pageable);

    // 내용에 특정 문자열을 포함하는 게시글을 페이징하여 조회
    Page<Board> findByContentContaining(String content, Pageable pageable);

    // 제목 또는 내용에 특정 문자열을 포함하는 게시글을 페이징하여 조회
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

}
