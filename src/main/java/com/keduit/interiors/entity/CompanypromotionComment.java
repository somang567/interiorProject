package com.keduit.interiors.entity;

import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "companypromotion_comments")
public class CompanypromotionComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 내용
    @Column(nullable = false)
    private String content;

    // 작성자 이름
    @Column(nullable = false)
    private String authorName;

    // 생성일시
    private LocalDateTime createdAt;

    // 수정일시
    private LocalDateTime updatedAt;

    // Companypromotion과의 관계 설정 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companypromotion_id")
    private Companypromotion companypromotion;

    // 사용자 엔티티와의 관계 (선택 사항)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
