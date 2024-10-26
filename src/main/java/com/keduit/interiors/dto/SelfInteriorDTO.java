package com.keduit.interiors.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelfInteriorDTO {

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

}
