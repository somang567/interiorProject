
package com.keduit.interiors.dto;

import com.keduit.interiors.entity.Megazine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class MegazineDTO {
//매거진 글 등록 관련 DTO

  private Long mno; //메거진 id

  private String title; //제목

  private String user;  //글 등록한 사용자

  private String content; //내용

  private int viewCount;  //조회수

  private int commentCount; // 댓글 수

  private int scrapCount; //스크랩 수

  private String oriImgName;  //원본 이미지 이름 사용자가 올린 원본 파일 이미지 이름
  //
  private String imgName; //이미지 이름 UUID + 확장자(oriImgName 여기서 긁어옴)
  //
  private String imageUrl;  //이미지 Url 경로 어플리케이션 프로퍼티스 + fileName 둘 다 들어가는거

  private LocalDateTime regTime; // 등록 시간
  private LocalDateTime updateTime; //수정 시간

  public MegazineDTO() {

  }

  public MegazineDTO(Long mno, String title, String user, String content, int viewCount, int commentCount, int scrapCount, String oriImgName, String imgName, String imageUrl, LocalDateTime regTime, LocalDateTime updateTime, List<ItemImgDTO> itemImgDTOList, List<Long> itemImgIds) {
    this.mno = mno;
    this.title = title;
    this.user = user;
    this.content = content;
    this.viewCount = viewCount;
    this.commentCount = commentCount;
    this.scrapCount = scrapCount;
    this.oriImgName = oriImgName;
    this.imgName = imgName;
    this.imageUrl = imageUrl;
    this.regTime = regTime; // 추가된 부분
    this.updateTime = updateTime; // 추가된 부분
    this.itemImgDTOList = itemImgDTOList;
    this.itemImgIds = itemImgIds;
  }

  //이거 쓰려면 조건: 의존성 추가. 모델명과 같아야 함
  //데이터 전송 객체(DTO)와 데이터베이스 엔티티 간의 변환을 매핑
  private static ModelMapper modelMapper = new ModelMapper();

  public Megazine createMegazine(){
    return modelMapper.map(this, Megazine.class);
  }

  //item을 -> ItemDTO 로 매핑
  public Megazine createItem(){
    return modelMapper.map(this, Megazine.class);
  }

  //Java에서 ModelMapper라는 라이브러리를 사용하여 객체 간의 변환을 수행하기 위한 객체를 초기화하는 코드
  //객체의 속성을 간편하게 매핑할 수 있습니다.
  public static MegazineDTO of(Megazine item){
    return modelMapper.map(item, MegazineDTO.class);
  }











  // 기본 이외의 것 ========================================================
  //ItemImgDTO 이걸 리스트로 받아서 여기서 배열을 만듦.
  private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

  //수정인 경우에는 읽어오기 때문에
  private List<Long> itemImgIds = new ArrayList<>();




}


