package com.keduit.interiors.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String content;
    private Long authorId;  // 작성자의 ID
    private String authorName; // 작성자의 이름
    private Long boardId;  // 댓글이 속한 게시글의 ID
    private LocalDateTime createdDate;
    private boolean isEditable; // 댓글 수정 가능 여부

    public CommentDTO() {

    }

    public CommentDTO(Long id, String content, Long authorId, String authorName, Long boardId) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.boardId = boardId;
    }

}
