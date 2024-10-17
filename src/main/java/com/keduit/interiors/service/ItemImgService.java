package com.keduit.interiors.service;


import com.keduit.interiors.entity.ItemImg;
import com.keduit.interiors.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional  //join이 걸려있기 때문에
public class ItemImgService {

  //crud하는 애
  private final ItemImgRepository itemImgRepository;
  private final FileService fileService;

  //application.properties 안에 선언해 놓은 친구를 데려옴
  @Value("${itemImgLocation}")
  private String itemImgLocation; //애플리케이션 프로퍼티에서 등록한 변수를 가져옴

  public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
    //이미지 파일이 들어왔으면
    if(!itemImgFile.isEmpty()){
      ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
          .orElseThrow(EntityNotFoundException::new);

      if(!StringUtils.isEmpty(savedItemImg.getImgName())){
        fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
      }

      //기존 이미지를 삭제하고 옴
      String oriImgName = itemImgFile.getOriginalFilename();
      String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
      String imgUrl = "/Users/juntk/MyFinalPoject/uploads/images" + imgName;
      savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
    }
  }

  public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
    String originalFileName = itemImgFile.getOriginalFilename();
    System.out.println("original -------------> " + originalFileName);
    String imgName = "";  //경로를 가져오아서
    String imgUrl = "";

    //특정 조건에 따라 이미지를 서버에 저장하고 경로를 설정하는 로직. 파일 업로드
    //String 과 관련해서 강력하게 가지고 있는 메서드?
    //originalFileName 이 널이거나 빈 문자열인지 확인
    if(!StringUtils.isEmpty(originalFileName)){
      //itemImgLocation 이친구가 uploadPathpath임, 파일 위치, 오리지날 파일 이름, 바이트 배열(이미지 파일이니까)
      imgName = fileService.uploadFile(itemImgLocation, originalFileName, itemImgFile.getBytes());
      imgUrl ="/Users/juntk/MyFinalPoject/uploads/images" + imgName;  //cONFIGURE 파일에서 선언되어 있음.
    }

    //상품 이미지 정보 저장
    //상품 이미지 등록
    itemImg.updateItemImg(originalFileName, imgName, imgUrl);
    itemImgRepository.save(itemImg);

    //
  }



}
