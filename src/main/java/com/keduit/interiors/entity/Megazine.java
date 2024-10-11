/*
package com.keduit.interiors.entity;

import com.keduit.interiors.entity.BaseEntity;

import javax.persistence.*;
import java.lang.reflect.Member;

public class Megazine extends BaseEntity {

  @Id
  @Column(name = "magazine_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;


  @Column(nullable = false, length = 255)
  private String title;

  @Column(nullable = false, length = 5000)
  private String content;

  @Column(nullable = false)
  private int viewCount;

  @Column(nullable = false)
  private int scrapCount;

  @Column(nullable = false, length = 500)
  private String oriImgName;

  @Column(nullable = false, length = 50)
  private String imgName;

  @Column(nullable = false, length = 500)
  private String imageUrl;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="member_id")
  private Member member;
}

 */