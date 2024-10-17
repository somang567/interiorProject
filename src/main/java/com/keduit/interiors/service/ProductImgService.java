package com.keduit.interiors.service;

import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.ProductImg;
import com.keduit.interiors.repository.ProductImgRepository;
import com.keduit.interiors.repository.ProductRepository;
import com.keduit.interiors.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImgService {
	private final ProductImgRepository productImgRepository;
	private final FileService fileService;

	@Value("${itemImgLocation}")
	private String productImgLocation;

	// 상품 이미지 저장
	public void saveProductImg(ProductImg productImg, MultipartFile productImgFile) throws Exception {

		String originalFileName = productImgFile.getOriginalFilename();
		System.out.println("original -------------> " + originalFileName);
		String imgName = "";
		String fileUrl = "";

		// 파일 업로드
		if (!StringUtils.isEmpty(originalFileName)) {
			imgName = fileService.uploadFile(productImgLocation, originalFileName, productImgFile.getBytes());
			// 웹 경로로 수정
			fileUrl = "/item/" + imgName;
		}

		// 상품 이미지 정보 업데이트 및 저장
		productImg.updateItemImg(originalFileName, imgName, fileUrl);
		productImgRepository.save(productImg);
	}

	// 상품 이미지 수정
	public void updateProductImg(Long productImgId, MultipartFile productImgFile) throws Exception {
		if (!productImgFile.isEmpty()) {
			ProductImg savedProductImg = productImgRepository.findById(productImgId)
					.orElseThrow(EntityNotFoundException::new);

			// 기존 이미지 삭제
			if (!StringUtils.isEmpty(savedProductImg.getImgName())) {
				try {
					fileService.deleteFile(productImgLocation + "/" + savedProductImg.getImgName());
				} catch (IOException e) {
					System.err.println("파일 삭제 실패: " + e.getMessage());
					throw new RuntimeException("파일 삭제 실패", e);
				}
			}

			// 새 이미지 업로드 및 정보 업데이트
			String oriImgName = productImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(productImgLocation, oriImgName, productImgFile.getBytes());
			// 새 파일의 웹 경로로 설정
			String fileUrl = "/item/" + imgName;
			savedProductImg.updateItemImg(oriImgName, imgName, fileUrl);
		}
	}

}
