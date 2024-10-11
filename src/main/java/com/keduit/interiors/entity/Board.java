package com.keduit.interiors.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String author;
    private String filename;
    private String filepath;

    @Column(updatable = false)
    private LocalDateTime createdDate; // 작성일자

    private LocalDateTime modifiedDate; // 수정일자

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now(); // 생성 시 작성일자 설정
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedDate = LocalDateTime.now(); // 수정 시 수정일자 설정
    }
}
