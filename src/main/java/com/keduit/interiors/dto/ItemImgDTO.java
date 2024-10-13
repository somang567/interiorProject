package com.keduit.interiors.dto;


import com.keduit.interiors.entity.ItemImg;
import com.keduit.interiors.entity.Megazine;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class ItemImgDTO {

  private Long id;

  private String imgName;

  private String oriImgName;

  private String imgUrl;

  private String regImgYn;  //등록 여부

  //이거 쓰는 거는 선택임.
  //ModelMapper는 객체 간의 매핑을 자동으로 처리해주는 라이브러리. 인스턴스로 생성.
  //이를 통해 DTO(Data Transfer Object)와 엔티티 간의 변환 작업이 수월해집니다.
  private static ModelMapper modelMapper = new ModelMapper();

  //item을 itemDTO로 바꿔줄때 매핑해줄 때 사용
  //of라는 이름의 정적 메서드를 정의합니다. 이 메서드는 ItemImg 객체를 파라미터로 받아서 ItemImgDTO 객체로 변환합니다.
  public static ItemImgDTO of(Megazine megazine){
    //from itemImg ~ to ItemImgDTO.class(이걸로 이 타입으로 매핑할거야)/ to 는 .class를 붙여눠야함
    return modelMapper.map(megazine, ItemImgDTO.class);  //itemImg -> ItemImgDTO로 매핑
  }

}
