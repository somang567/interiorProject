package com.keduit.interiors.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Board extends BaseEntity {

    @Id
    private Long id; // 게시글 번호 (수동 관리)

    private String title; // 제목
    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author; // 작성자 (Member와 관계 설정)

    // 이미지 파일 관련 필드
    private String imageFilename;  // 이미지 파일명
    private String imagePath;      // 이미지 저장 경로
    private String filename;  // 파일명 필드
    private String filepath;   // 파일 경로

    @Column(nullable = false)
    private int commentCount = 0;
    @Column(nullable = false)
    private int viewCount = 0;

    // 첨부 파일 목록 관리
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attachment> attachments;

    // 댓글 목록 관리
    @OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;

}
