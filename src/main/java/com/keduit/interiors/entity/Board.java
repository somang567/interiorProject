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
    private String filename;
    private String filepath;

    // Attachments 필드 추가
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attachment> attachments; // 첨부 파일 목록

    // Getters nd Setaters는 @Data 어노테이션으로 자동 생성됨
}
