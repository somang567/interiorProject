package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글 ID로 댓글 조회
    List<Comment> findByBoardId(Long boardId);

}