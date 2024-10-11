package com.keduit.interiors.dto;

import com.keduit.interiors.entity.Megazine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class MegazineDTO {


  private Long mno; //메거진 id

  private String title; //제목

  private String content; //내용

  private int viewCount;  //조회수

  private int scrapCount; //스크랩 수

  private String oriImgName;  //원본 이미지 이름

  private String imgName; //이미지 이름

  private String imageUrl;  //이미지 Url

  private static ModelMapper modelMapper = new ModelMapper();

  public Megazine createMegazine(){
    return modelMapper.map(this, Megazine.class);
  }

  //item을 -> ItemDTO 로 매핑
  public Megazine createBoard(){
    return modelMapper.map(this, Megazine.class);
  }

  //Java에서 ModelMapper라는 라이브러리를 사용하여 객체 간의 변환을 수행하기 위한 객체를 초기화하는 코드
  //객체의 속성을 간편하게 매핑할 수 있습니다.
  public static MegazineDTO of(Megazine item){
    return modelMapper.map(item, MegazineDTO.class);
  }

}

