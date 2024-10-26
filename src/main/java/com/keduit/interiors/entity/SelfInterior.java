package com.keduit.interiors.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class SelfInterior extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 10000000)
    private String content;

    @Column(nullable = false)
    private int commentCount = 0;

    @Column(nullable = false)
    private int viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    private String filename;

    private String filepath;

    @OneToMany(mappedBy = "selfInterior", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelfInteriorComment> comments = new ArrayList<>();


}
