package com.keduit.interiors.dto;

import com.keduit.interiors.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MegazineCommentDTO extends BaseEntity {

    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long megazineId;
    private LocalDateTime commentCreatedTime;

    public MegazineCommentDTO() {

    }

    public MegazineCommentDTO(Long id, String commentWriter, String commentContents, Long megazineId) {
        this.id = id;
        this.commentWriter = commentWriter;
        this.commentContents = commentContents;
        this.megazineId = megazineId;
    }



}
