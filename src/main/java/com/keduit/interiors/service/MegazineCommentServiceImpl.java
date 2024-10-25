package com.keduit.interiors.service;

import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.dto.MegazineCommentDTO;
import com.keduit.interiors.entity.*;
import com.keduit.interiors.repository.MegazineCommentRepository;
import com.keduit.interiors.repository.MegazineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MegazineCommentServiceImpl implements MegazineCommentService {


    private final MegazineRepository megazineRepository;
    private final MegazineCommentRepository megazineCommentRepository;

    @Override
    public MegazineCommentDTO saveComment(MegazineCommentDTO megazineCommentDTO, Member member) {
        // 게시글 존재 여부 확인
        Megazine megazine = megazineRepository.findById(megazineCommentDTO.getMegazineId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 새로운 댓글 생성
        MegazineComment megazineComment = new MegazineComment();
        megazineComment.setContent(megazineCommentDTO.getContent());
        megazineComment.setAuthor(member); // 작성자를 Member로 설정
        megazineComment.setMegazine(megazine);
        megazineComment.setCreatedDate(LocalDateTime.now());

        // 댓글 저장
        MegazineComment savedComment = megazineCommentRepository.save(megazineComment);

        // 댓글 저장 후 댓글 수 증가
        megazine.setCommentCount(megazine.getCommentCount() + 1); // 댓글 수 증가
        megazineRepository.save(megazine); // 댓글 수 업데이트

        // 저장된 댓글을 DTO로 변환하여 반환
        MegazineCommentDTO savedCommentDTO = mapToDTO(savedComment);

        return savedCommentDTO;
    }

    @Override
    public MegazineCommentDTO getCommentById(Long megazineId) {
        MegazineComment megazineComment = megazineCommentRepository.findById(megazineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        return mapToDTO(megazineComment);
    }



//@Override
//public MegazineCommentDTO getMegazineCommentById(Long megazineId) {
//        MegazineComment megazineComment = megazineCommentRepository.findById(megazineId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
//        return mapToDTO(megazineComment);
//    }

    @Override
    public List<MegazineCommentDTO> getCommentsByMegazineId(Long megazineId) {
        List<MegazineComment> comments = megazineCommentRepository.findByMegazineMno(megazineId);
        return comments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public MegazineCommentDTO getMegazineCommentById(Long megazineId) {
        MegazineComment comment = megazineCommentRepository.findById(megazineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        return mapToDTO(comment);
    }


//이상한 애들 리스트...
//    @Override
//    public MegazineCommentDTO getMegazineCommentById(Long megazineId) {
//        Optional<MegazineComment> megazineComment = megazineCommentRepository.findById(megazineId);
//        return megazineCommentRepository.findById(megazineId)
//                .map(this::mapToDTO) // 댓글이 존재할 경우 DTO로 변환
//                .orElse(null); // 댓글이 존재하지 않을 경우 null 반환
//    }

//    @Override
//    public MegazineCommentDTO getMegazineCommentById(Long megazineId) {
//        return megazineCommentRepository.findById(megazineId)
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }


    @Override
    public void updateComment(MegazineCommentDTO megazineCommentDTO, Member member) throws IllegalAccessException {
        MegazineComment megazineComment = megazineCommentRepository.findById(megazineCommentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        // 작성자 또는 관리자 확인
        if (!isAuthorOrAdmin(megazineComment, member)) {
            throw new IllegalAccessException("해당 댓글을 수정할 권한이 없습니다.");
        }

        megazineComment.setContent(megazineCommentDTO.getContent());
        megazineCommentRepository.save(megazineComment);
    }

    @Override
    public void deleteComment(Long megazineId, Member member) throws IllegalAccessException {
        MegazineComment megazineComment = megazineCommentRepository.findById(megazineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        // 작성자 또는 관리자 확인
        if (!isAuthorOrAdmin(megazineComment, member)) {
            throw new IllegalAccessException("해당 댓글을 삭제할 권한이 없습니다.");
        }

        // 댓글 수 감소 처리
        Megazine megazine = megazineComment.getMegazine();
        megazine.setCommentCount(megazine.getCommentCount() - 1);
        megazineRepository.save(megazine);  // 댓글 수 업데이트

        megazineCommentRepository.delete(megazineComment);
    }

    // 작성자 또는 관리자 확인 메서드
    private boolean isAuthorOrAdmin(MegazineComment comment, Member member) {
        return comment.getAuthor().getId().equals(member.getId()) || isAdmin(member);
    }

    // 관리자인지 확인하는 메서드
    private boolean isAdmin(Member member) {
        return member.getRole().equals("ROLE_ADMIN");
    }


    // 댓글 엔티티를 DTO로 변환하는 메서드
    private MegazineCommentDTO mapToDTO(MegazineComment megazineComment) {
        MegazineCommentDTO dto = new MegazineCommentDTO();
        dto.setId(megazineComment.getId());
        dto.setContent(megazineComment.getContent());
        dto.setAuthorId(megazineComment.getAuthor().getId()); // 작성자의 ID 설정
        dto.setAuthorName(megazineComment.getAuthor().getName()); // 작성자의 이름 설정
        dto.setMegazineId(megazineComment.getMegazine().getMno());
        dto.setCreatedDate(megazineComment.getCreatedDate());
        return dto;
    }


//    public List<MegazineCommentDTO>findAll(Long megazineId){
//        Megazine megazine = megazineRepository.findById(megazineId).get();
//        List<MegazineComment> megazineCommentList = megazineCommentRepository.findAllByMegazineOrderByIdDesc(megazine);
//        List<MegazineCommentDTO> commentDTOList = new ArrayList<>();
//        for(MegazineComment megazineComment : megazineCommentList){
//            MegazineCommentDTO megazineCommentDTO = MegazineCommentDTO.toCommentDTO(megazineComment, megazineId);
//            commentDTOList.add(megazineCommentDTO);
//        }
//        return commentDTOList;
//    }

}

