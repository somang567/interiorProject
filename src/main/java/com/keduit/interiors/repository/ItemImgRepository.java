package com.keduit.interiors.repository;

import com.keduit.interiors.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg,Long> {
  //List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);  //상품 쭉 읽어옴 ascnding으로 읽어줘
  List<ItemImg> findByMegazine_MnoOrderByIdAsc(Long mno);
  //대표이미지만 가져오는 추상 메서드
  ItemImg findByMegazine_MnoAndRegImgYn(Long mno, String regImgYn);
}
