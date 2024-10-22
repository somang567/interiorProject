package com.keduit.interiors.service;

import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.dto.MegazineCommentDTO;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.MegazineComment;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.MegazineCommentRepository;
import com.keduit.interiors.repository.MegazineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MegazineCommentService {

    MegazineCommentDTO saveComment(MegazineCommentDTO megazineCommentDTO, Member member);

    MegazineCommentDTO getCommentById(Long id);

    //////////////////////
    MegazineCommentDTO getMegazineCommentById(Long id);

    void updateComment(MegazineCommentDTO megazineCommentDTO, Member member) throws IllegalAccessException;

    void deleteComment(Long id, Member member) throws IllegalAccessException;

    List<MegazineCommentDTO> getCommentsByMegazineId(Long megazineMno);
}