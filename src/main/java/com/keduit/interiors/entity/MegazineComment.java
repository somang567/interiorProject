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
    @Column(name = "megacomment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 200)
    private String commentWriter;

    @Column(nullable = true, length = 5000)
    private String commentContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "megazine_id") //외래키 설정
    private Megazine megazine;

    //private LocalDateTime commentCreatedTime;

    public static MegazineComment toSaveEntity(MegazineCommentDTO megazineCommentDTO, Megazine megazine) {
        MegazineComment megazineComment = new MegazineComment();
        megazineComment.setCommentWriter(megazineCommentDTO.getCommentWriter());
        megazineComment.setCommentContents(megazineCommentDTO.getCommentContents());
        megazineComment.setMegazine(megazine);

        return megazineComment;
    }

}
