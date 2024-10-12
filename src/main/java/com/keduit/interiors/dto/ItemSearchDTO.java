package com.keduit.interiors.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemSearchDTO {

  private String searchDateTye;  //전체기간/ 1일 /1주 /1개월

  private String searchBy;  //상품명 or 등록자

  private String searchQuery = "";  //검색어를 입력해주세요
}
