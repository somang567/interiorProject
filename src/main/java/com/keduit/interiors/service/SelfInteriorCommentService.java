package com.keduit.interiors.service;

import com.keduit.interiors.constant.Role;
import com.keduit.interiors.dto.SelfInteriorCommentDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.entity.SelfInterior;
import com.keduit.interiors.entity.SelfInteriorComment;
import com.keduit.interiors.repository.SelfInteriorCommentRepository;
import com.keduit.interiors.repository.SelfInteriorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SelfInteriorCommentService {

    private static final Logger logger = LoggerFactory.getLogger(SelfInteriorCommentService.class);

    @Autowired
    private SelfInteriorCommentRepository selfInteriorCommentRepository;

    @Autowired
    private SelfInteriorRepository selfInteriorRepository;

    // 댓글 추가 메서드
    @Transactional
    public SelfInteriorCommentDTO addComment(SelfInteriorCommentDTO commentDTO, Member member) {
        SelfInterior selfInterior = selfInteriorRepository.getById(commentDTO.getSelfInteriorId());

        SelfInteriorComment comment = dtoToEntity(commentDTO);
        comment.setAuthor(member);
        comment.setSelfInterior(selfInterior);

        selfInterior.setCommentCount(selfInterior.getCommentCount() + 1);

        selfInteriorCommentRepository.save(comment);
        logger.info("Comment {} added to SelfInterior {} by user {}", comment.getId(), selfInterior.getId(), member.getEmail());

        return entityToDto(comment);
    }

    // 댓글 조회 메서드
    public List<SelfInteriorCommentDTO> getCommentsBySelfInteriorId(Long selfInteriorId) {
        List<SelfInteriorComment> comments = selfInteriorCommentRepository.findBySelfInteriorId(selfInteriorId);
        return comments.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    // 댓글 수정 메서드
    @Transactional
    public void updateComment(SelfInteriorCommentDTO commentDTO, Member member) throws AccessDeniedException {
        SelfInteriorComment comment = selfInteriorCommentRepository.getById(commentDTO.getId());

        boolean isAdmin = member.getRole() == Role.ADMIN;

        logger.info("User {} attempting to update comment {}", member.getEmail(), comment.getId());
        logger.info("Is Admin: {}", isAdmin);

        if (comment.getAuthor().equals(member) || isAdmin) {
            comment.setContent(commentDTO.getContent());
            selfInteriorCommentRepository.save(comment);
            logger.info("Comment {} updated by user {}", comment.getId(), member.getEmail());
        } else {
            logger.warn("User {} attempted to update comment {} without permission", member.getEmail(), comment.getId());
            throw new AccessDeniedException("해당 댓글을 수정할 권한이 없습니다.");
        }
    }

    // 댓글 삭제 메서드
    @Transactional
    public void deleteComment(Long commentId, Member member) throws AccessDeniedException {
        SelfInteriorComment comment = selfInteriorCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 댓글 ID: " + commentId));

        boolean isAdmin = member.getRole() == Role.ADMIN;

        logger.info("User {} attempting to delete comment {}", member.getEmail(), commentId);
        logger.info("Is Admin: {}", isAdmin);

        if (comment.getAuthor().equals(member) || isAdmin) {
            SelfInterior selfInterior = comment.getSelfInterior();
            selfInterior.setCommentCount(selfInterior.getCommentCount() - 1);
            selfInteriorCommentRepository.deleteById(commentId);
            logger.info("Comment {} deleted by user {}", commentId, member.getEmail());
        } else {
            logger.warn("User {} attempted to delete comment {} without permission", member.getEmail(), commentId);
            throw new AccessDeniedException("해당 댓글을 삭제할 권한이 없습니다.");
        }
    }

    // 엔티티 -> DTO 변환 메서드
    public SelfInteriorCommentDTO entityToDto(SelfInteriorComment comment) {
        return new SelfInteriorCommentDTO(
                comment.getId(),
                comment.getSelfInterior().getId(),
                comment.getContent(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getRegTime(),
                comment.getUpdateTime()
        );
    }

    // DTO -> 엔티티 변환 메서드
    public SelfInteriorComment dtoToEntity(SelfInteriorCommentDTO dto) {
        SelfInteriorComment comment = new SelfInteriorComment();
        comment.setId(dto.getId());
        comment.setContent(dto.getContent());
        // selfInterior와 author는 서비스에서 설정
        comment.setRegTime(dto.getRegTime());
        comment.setUpdateTime(dto.getUpdateTime());
        return comment;
    }
}
