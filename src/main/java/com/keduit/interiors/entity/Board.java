package com.keduit.interiors.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String author;

    // 이미지 파일 관련 필드
    private String imageFilename;  // 이미지 파일명
    private String imagePath;      // 이미지 저장 경로
    private String filename;  // 새로 추가한 파일명 필드
    private String filepath;   // 새로 추가한 파일 경로 필드

    // 첨부 파일 목록 관리 (이미지 외의 추가 첨부 파일도 관리 가능)
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attachment> attachments;

    // @Data로 인해 getter/setter는 자동으로 생성됩니다.
}
