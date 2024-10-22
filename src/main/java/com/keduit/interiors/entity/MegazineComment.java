package com.keduit.interiors.entity;

import com.keduit.interiors.dto.MegazineCommentDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
public class MegazineComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author; // 작성자 (Member와 관계 설정)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "megazine_id")
    private Megazine megazine;

    private LocalDateTime createdDate;

/*
    public static MegazineComment toSaveEntity(MegazineCommentDTO megazineCommentDTO, Megazine megazine) {
        MegazineComment megazineComment = new MegazineComment();
        megazineComment.setCommentWriter(megazineCommentDTO.getCommentWriter());
        megazineComment.setCommentContents(megazineCommentDTO.getCommentContents());
        megazineComment.setMegazine(megazine);

        return megazineComment;
    }

 */

}
