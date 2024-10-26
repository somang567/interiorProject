package com.keduit.interiors.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SelfInteriorCommentDTO {

    private Long id;
    private Long selfInteriorId;
    private String content;
    private Long authorId;
    private String authorName;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    public SelfInteriorCommentDTO() {}

    public SelfInteriorCommentDTO(Long id, Long selfInteriorId, String content, Long authorId, String authorName, LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.selfInteriorId = selfInteriorId;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }
}
