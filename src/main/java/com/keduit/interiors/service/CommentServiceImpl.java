package com.keduit.interiors.service;

import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.entity.Board;
import com.keduit.interiors.entity.Comment;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.BoardRepository;
import com.keduit.interiors.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 댓글 저장 메서드
    @Override
    public CommentDTO saveComment(CommentDTO commentDTO, Member member) {
        // 게시글 존재 여부 확인
        Board board = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 새로운 댓글 생성
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(member); // 작성자를 Member로 설정
        comment.setBoard(board);
        comment.setCreatedDate(LocalDateTime.now());

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        // 댓글 저장 후 댓글 수 증가
        board.setCommentCount(board.getCommentCount() + 1); // 댓글 수 증가
        boardRepository.save(board); // 댓글 수 업데이트

        // 저장된 댓글을 DTO로 변환하여 반환
        CommentDTO savedCommentDTO = mapToDTO(savedComment);

        return savedCommentDTO;
    }


    // 특정 게시글에 달린 모든 댓글을 조회하는 메서드
    @Override
    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return comments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // 특정 댓글을 ID로 조회하여 DTO로 반환하는 메서드
    @Override
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        return mapToDTO(comment);
    }

    // 댓글 수정 메서드
    @Override
    public void updateComment(CommentDTO commentDTO, Member member) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        // 작성자 또는 관리자 확인
        if (!isAuthorOrAdmin(comment, member)) {
            throw new IllegalAccessException("해당 댓글을 수정할 권한이 없습니다.");
        }

        comment.setContent(commentDTO.getContent());
        commentRepository.save(comment);
    }

    // 댓글 삭제 메서드
    @Override
    public void deleteComment(Long id, Member member) throws IllegalAccessException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        // 작성자 또는 관리자 확인
        if (!isAuthorOrAdmin(comment, member)) {
            throw new IllegalAccessException("해당 댓글을 삭제할 권한이 없습니다.");
        }

        // 댓글 수 감소 처리
        Board board = comment.getBoard();
        board.setCommentCount(board.getCommentCount() - 1);
        boardRepository.save(board);  // 댓글 수 업데이트

        commentRepository.delete(comment);
    }

    // 작성자 또는 관리자 확인 메서드
    private boolean isAuthorOrAdmin(Comment comment, Member member) {
        return comment.getAuthor().getId().equals(member.getId()) || isAdmin(member);
    }

    // 관리자인지 확인하는 메서드
    private boolean isAdmin(Member member) {
        return member.getRole().equals("ROLE_ADMIN");
    }

    // 댓글 엔티티를 DTO로 변환하는 메서드
    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthorId(comment.getAuthor().getId()); // 작성자의 ID 설정
        dto.setAuthorName(comment.getAuthor().getName()); // 작성자의 이름 설정
        dto.setBoardId(comment.getBoard().getId());
        dto.setCreatedDate(comment.getCreatedDate());
        return dto;
    }
}

