package com.keduit.interiors.service;

import com.keduit.interiors.dto.MegazineCommentDTO;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.MegazineComment;
import com.keduit.interiors.repository.MegazineCommentRepository;
import com.keduit.interiors.repository.MegazineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MegazineCommentService {

    private final MegazineRepository megazineRepository;
    private final MegazineCommentRepository megazineCommentRepository;

    public Long saveComment(MegazineCommentDTO megazineCommentDTO) {

        if (megazineCommentDTO.getId() != null) {
            // findById를 호출할 때는 반드시 ID가 null이 아닌지 체크
            megazineCommentRepository.findById(megazineCommentDTO.getId());
        }

        Optional<Megazine> optionalMegazine = megazineRepository.findById(megazineCommentDTO.getId());
        if(optionalMegazine.isPresent()) {
            Megazine megazine = optionalMegazine.get();
            MegazineComment megazineComment = MegazineComment.toSaveEntity(megazineCommentDTO, megazine);
            return megazineCommentRepository.save(megazineComment).getId();
        }else{
            return null;
        }
    }

    public List<MegazineCommentDTO> findAll(long megazineId) {

        Megazine megazine = megazineRepository.findById(megazineId).get();
        List<MegazineComment> megazineCommentList = megazineCommentRepository.findAllByMegazineOrderByIdDesc(megazine);
        List<MegazineCommentDTO> megazineCommentDTOList = new ArrayList<>();
        for (MegazineComment megazineComment: megazineCommentList){
            MegazineCommentDTO megazineCommentDTO = MegazineCommentDTO.toCommentDTO(megazineComment, megazineId);
            megazineCommentDTOList.add(megazineCommentDTO);
        }
        return megazineCommentDTOList;
    }
}