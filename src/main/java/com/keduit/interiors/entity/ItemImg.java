package com.keduit.interiors.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class ItemImg extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="item_img_id")
  private Long id;

  private String imgName;     //이미지 파일 명

  private String oriImgName;  //원본 이미지 이름

  private String imgUrl;      //이미지 조회 경로

  private String regImgYn;    //대표 이미지 여부

  // 나는 다 아이템은 한개
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="megazine_id")
  private Megazine megazine;

  //새로 업데이트 하는
  //3개 주면 자기 자신에게 넣어줌
  //엔티티 매니저가 감지
  public void updateItemImg(String oriImgName, String imgName, String imgUrl){
    this.oriImgName = oriImgName;
    this.imgName = imgName;
    this.imgUrl = imgUrl;
  }
}
