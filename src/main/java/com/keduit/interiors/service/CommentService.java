package com.keduit.interiors.service;

import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.entity.Board;
import com.keduit.interiors.entity.Comment;
import com.keduit.interiors.repository.BoardRepository;
import com.keduit.interiors.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 저장
    public void saveComment(CommentDTO commentDTO) {
        // 게시글을 찾음
        Board board = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 댓글 생성
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(commentDTO.getAuthor());
        comment.setBoard(board);
        comment.setCreatedDate(LocalDateTime.now());

        commentRepository.save(comment);  // 댓글 저장
    }

    // 특정 게시글에 달린 댓글 조회
    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);

        // 댓글 리스트를 DTO로 변환하여 반환
        return comments.stream().map(comment -> {
            CommentDTO dto = new CommentDTO();
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());
            dto.setAuthor(comment.getAuthor());
            dto.setBoardId(comment.getBoard().getId());
            dto.setCreatedDate(comment.getCreatedDate());
            return dto;
        }).collect(Collectors.toList());
    }

    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthor(comment.getAuthor());
        dto.setBoardId(comment.getBoard().getId());
        dto.setCreatedDate(comment.getCreatedDate());
        return dto;
    }

    public void updateComment(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        comment.setContent(commentDTO.getContent());
        commentRepository.save(comment);
    }


    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }
}
