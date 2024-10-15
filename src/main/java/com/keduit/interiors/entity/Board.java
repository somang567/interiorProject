package com.keduit.interiors.entity;

import lombok.Data;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 번호

    private String title; // 제목
    private String content; // 내용
    private String author; // 작성자

    // 이미지 파일 관련 필드
    private String imageFilename;  // 이미지 파일명
    private String imagePath;      // 이미지 저장 경로
    private String filename;  // 새로 추가한 파일명 필드
    private String filepath;   // 새로 추가한 파일 경로

    @Column(nullable = false)
    private int commentCount = 0;
    @Column(nullable = false)
    private int viewCount = 0;

    // 첨부 파일 목록 관리 (이미지 외의 추가 첨부 파일도 관리 가능)
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attachment> attachments;

}
