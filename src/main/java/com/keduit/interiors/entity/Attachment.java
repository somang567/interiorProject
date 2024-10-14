package com.keduit.interiors.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename; // 파일 이름
    private String filepath; // 파일 경로

    @ManyToOne // 여러 개의 Attachment가 하나의 Board에 속할 수 있음
    @JoinColumn(name = "board_id") // 외래 키 설정
    private Board board; // Board와의 관계
}
