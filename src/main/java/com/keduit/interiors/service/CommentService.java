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

    // CommentRepository와 BoardRepository를 주입받음
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 댓글 저장 메서드
    public void saveComment(CommentDTO commentDTO) {
        // 댓글이 달린 게시글을 찾음
        Board board = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 새로운 댓글 생성 및 설정
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(commentDTO.getAuthor());
        comment.setBoard(board);
        comment.setCreatedDate(LocalDateTime.now());

        // 댓글 저장
        commentRepository.save(comment);

        // 댓글 수 증가 처리
        board.setCommentCount(board.getCommentCount() + 1);
        boardRepository.save(board);  // 댓글 수 업데이트
    }

    // 특정 게시글에 달린 모든 댓글을 조회하는 메서드
    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        // 게시글에 해당하는 댓글 리스트를 가져옴
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

    // 특정 댓글을 ID로 조회하여 DTO로 반환하는 메서드
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

    // 댓글 수정 메서드
    public void updateComment(CommentDTO commentDTO) {
        // 수정할 댓글을 찾음
        Comment comment = commentRepository.findById(commentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        // 댓글 내용 수정
        comment.setContent(commentDTO.getContent());
        // 변경된 댓글 저장
        commentRepository.save(comment);
    }

    // 댓글 삭제 메서드
    public void deleteComment(Long id) {
        // 삭제할 댓글을 찾음
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        // 댓글 삭제
        commentRepository.delete(comment);
    }
}
