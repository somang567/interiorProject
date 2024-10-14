
package com.keduit.interiors.entity;

import com.keduit.interiors.dto.MegazineDTO;
import com.keduit.interiors.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
public class Megazine extends BaseEntity {

  @Id
  @Column(name = "magazine_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;


  @Column(nullable = false, length = 500)
  private String title;

  @Column(nullable = false, length = 200)
  private String user;

  @Column(nullable = false, length = 5000)
  private String content;

  @Column(nullable = false)
  private int viewCount;

  @Column(nullable = false)
  private int scrapCount;

  @Column(nullable = false, length = 800)
  private String oriImgName;

  @Column(nullable = false, length = 800)
  private String imgName;

  @Column(nullable = false, length = 1000)
  private String imageUrl;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "member_id")
  private Member member;


  //엔티티에다 바로 넣어줌
  public void updateItem(MegazineDTO megazineDTO) {
    this.title = megazineDTO.getTitle();
    this.content = megazineDTO.getContent();
  }
}
