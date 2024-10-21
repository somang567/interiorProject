
package com.keduit.interiors.entity;

import com.keduit.interiors.dto.MegazineCommentDTO;
import com.keduit.interiors.dto.MegazineDTO;
import com.keduit.interiors.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
public class Megazine extends BaseEntity {

  @Id
  @Column(name="megazine_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;


  @Column(nullable = false, length = 500)
  private String title;

  //member.getemail 해서 가져오기
  @Column(nullable = true, length = 200)
  private String user;

  @Column(nullable = false, length = 5000)
  private String content;

  @Column(nullable = true)
  private int viewCount;

  @Column(nullable = true)
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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "megazine", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<MegazineComment> megazineCommentsList = new ArrayList<>();


//  @OneToMany(mappedBy = "magazine", cascade = CascadeType.REMOVE)
//  private List<MegazineScrap> magazineScrapList;


  //엔티티에다 바로 넣어줌
  public void updateItem(MegazineDTO megazineDTO) {
    this.title = megazineDTO.getTitle();
    this.content = megazineDTO.getContent();
    this.oriImgName = megazineDTO.getOriImgName();
    this.imgName = megazineDTO.getImgName();
    this.imageUrl = megazineDTO.getImageUrl();
  }
}
