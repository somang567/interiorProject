package com.keduit.interiors.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardDTO {

    private Long id; // 게시글 번호

    private String title; // 제목

    private String content; // 내용

    private Long authorId;  // 작성자의 ID
    private String authorName; // 작성자의 이름

    private String imageFilename;  // 이미지 파일명

    private String imagePath;   // 이미지 저장 경로

    private String filename;  // 파일명 필드

    private String filepath;   // 파일 경로

    private int commentCount; // 댓글 수

    private int viewCount; // 조회수

    private LocalDateTime regTime;  // 등록 시간

    private LocalDateTime updateTime; // 수정 시간

    public BoardDTO() {
    }

    public BoardDTO(Long id, String title, String content, Long authorId, String authorName,
                    String imageFilename, String imagePath, String filename,
                    String filepath, int commentCount, int viewCount,
                    LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.imageFilename = imageFilename;
        this.imagePath = imagePath;
        this.filename = filename;
        this.filepath = filepath;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }

}
