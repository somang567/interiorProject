package com.keduit.interiors.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String content;
    private String author;
    private Long boardId;  // 댓글이 속한 게시글의 ID
    private LocalDateTime createdDate;
    private boolean isEditable; // 댓글 수정 가능 여부

    public CommentDTO() {

    }

    public CommentDTO(Long id, String content, String author, Long boardId) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.boardId = boardId;
    }

}