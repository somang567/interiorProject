package com.keduit.interiors.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CompanypromotionDTO {

    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
    private String filename;
    private String filepath;
    private int commentCount;
    private int viewCount;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    public CompanypromotionDTO() {}

    public CompanypromotionDTO(Long id, String title, String content, Long authorId, String authorName,
                               String filename, String filepath, int commentCount, int viewCount,
                               LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.filename = filename;
        this.filepath = filepath;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }
}
