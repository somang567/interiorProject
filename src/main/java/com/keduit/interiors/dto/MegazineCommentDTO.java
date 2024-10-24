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
public class MegazineCommentDTO extends BaseEntity {

    private Long id;
    private String content;
    private Long authorId;  // 작성자의 ID
    private String authorName; // 작성자의 이름
    private Long megazineId;  // 댓글이 속한 게시글의 ID
    private LocalDateTime createdDate;
    private boolean isEditable; // 댓글 수정 가능 여부

    public MegazineCommentDTO() {

    }

    public MegazineCommentDTO(Long id, String content, Long authorId, String authorName, Long megazineId) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.megazineId = megazineId;
    }

/*
    public MegazineCommentDTO(Long id, String commentWriter, String commentContents, Long megazineId) {
        this.id = id;
        this.commentWriter = commentWriter;
        this.commentContents = commentContents;
        this.megazineId = megazineId;
    }


    public static MegazineCommentDTO toCommentDTO(MegazineComment megazineComment, Long megazineId ) {
        MegazineCommentDTO megazineCommentDTO = new MegazineCommentDTO();
        megazineCommentDTO.setId(megazineComment.getId());
        megazineCommentDTO.setCommentWriter(megazineComment.getCommentWriter());
        megazineCommentDTO.setCommentContents(megazineComment.getCommentContents());
        megazineCommentDTO.setMegazineId(megazineId);
        return megazineCommentDTO;
    }
*/

}
