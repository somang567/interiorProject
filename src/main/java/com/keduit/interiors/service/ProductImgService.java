package com.keduit.interiors.service;

import com.keduit.interiors.entity.ProductImg;
import com.keduit.interiors.repository.ProductImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImgService {
	private final ProductImgRepository productImgRepository;
	private final FileService fileService;

	// @Value: application.propertice에 등록한 변수의 값을 가져옴.
	@Value("${itemImgLocation}")
	private String itemImgLocation;

	public void saveProductImg(ProductImg productImg, MultipartFile productImgFile) throws Exception {

		String originalFileName = productImgFile.getOriginalFilename();
		String img_name = "";
		String file_Url = "";

		// 파일 업로드
		if (!StringUtils.isEmpty(originalFileName)) {
			img_name = fileService.uploadFile(itemImgLocation, originalFileName, productImgFile.getBytes());
			file_Url = "/images/item/" + img_name;
		}
		// 상품이미지 정보 저장
		productImg.updateItemImg(originalFileName, img_name, file_Url);
		productImgRepository.save(productImg);
	}

	public void updateProductImg(Long productImgId, MultipartFile itemImgFile) throws Exception{
		if (!itemImgFile.isEmpty()){
			ProductImg savedProductImg = productImgRepository.findById(productImgId)
					.orElseThrow(EntityNotFoundException::new);

			// 기존 이미지 삭제
			if(!StringUtils.isEmpty(savedProductImg.getImg_name())){
				fileService.deleteFile(itemImgLocation + "/" + savedProductImg.getImg_name());
			}

			String oriImgName = itemImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName,itemImgFile.getBytes());
			String imgUrl = "/images/item/" + imgName;
			savedProductImg.updateItemImg(oriImgName, imgName,imgUrl);
		}
	}
}
