package com.keduit.interiors.service;

import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.entity.Member;

import java.util.List;

public interface CommentService {
    CommentDTO saveComment(CommentDTO commentDTO, Member member);

    CommentDTO getCommentById(Long id);

    void updateComment(CommentDTO commentDTO, Member member) throws IllegalAccessException;

    void deleteComment(Long id, Member member) throws IllegalAccessException;

    List<CommentDTO> getCommentsByBoardId(Long boardId);
}
