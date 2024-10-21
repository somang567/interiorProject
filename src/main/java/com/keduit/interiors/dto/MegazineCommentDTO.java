package com.keduit.interiors.dto;

import com.keduit.interiors.entity.BaseEntity;
import com.keduit.interiors.entity.MegazineComment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MegazineCommentDTO extends BaseEntity {

    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long megazineId;

/*
    public MegazineCommentDTO(Long id, String commentWriter, String commentContents, Long megazineId) {
        this.id = id;
        this.commentWriter = commentWriter;
        this.commentContents = commentContents;
        this.megazineId = megazineId;
    }

 */
    public static MegazineCommentDTO toCommentDTO(MegazineComment megazineComment, Long megazineId ) {
        MegazineCommentDTO megazineCommentDTO = new MegazineCommentDTO();
        megazineCommentDTO.setId(megazineComment.getId());
        megazineCommentDTO.setCommentWriter(megazineComment.getCommentWriter());
        megazineCommentDTO.setCommentContents(megazineComment.getCommentContents());
        megazineCommentDTO.setMegazineId(megazineId);
        return megazineCommentDTO;
    }


}
