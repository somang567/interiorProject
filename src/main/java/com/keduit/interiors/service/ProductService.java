package com.keduit.interiors.service;

import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.Product;
import com.keduit.interiors.entity.ProductImg;
import com.keduit.interiors.repository.ProductRepository;
import com.keduit.interiors.service.ProductImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductImgService productImgService;


	// 상품과 여러 이미지를 저장하는 메서드
	@Transactional(rollbackFor = Exception.class)
	public Long addProduct(ProductDTO productDTO, List<MultipartFile> files) {
		// Product 엔티티 생성 및 저장
		Product product = productDTO.createProduct();
		productRepository.save(product); // Product 저장

		// 파일 업로드 처리 (다수의 이미지 파일 저장 처리)
		boolean isFirstImage = true; // 첫 번째 이미지를 구별하기 위한 플래그
		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					try {
						// ProductImg 엔티티 생성
						ProductImg productImg = new ProductImg();
						productImg.setProduct(product); // 상품과 연관 설정

						// 첫 번째 파일은 썸네일로 지정
						productImg.setThumbnail(isFirstImage);
						isFirstImage = false;

						// 상품에 이미지 추가 (연관관계 설정)
						product.addProductImage(productImg);

						// 파일을 저장하고 이미지 정보를 DB에 저장
						productImgService.saveProductImg(productImg, file);

					} catch (Exception e) {
						// 업로드 실패 시 런타임 예외 발생
						throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
					}
				}
			}
		}

		return product.getId(); // 저장된 product의 ID 반환
	}

	// 첫 번째 이미지 URL 가져오기
	public String getFirstImageUrl(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + id));

		// 썸네일로 지정된 이미지의 URL을 스트림으로 가져옴
		return product.getProductImgList().stream()
				.filter(ProductImg::isThumbnail)
				.map(ProductImg::getFileUrl)
				.findFirst()
				.orElse(null); // 썸네일이 없는 경우 null 반환
	}

	public ProductDTO getProductById(Long productId) {
		// 상품 조회
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

		// 상품을 DTO로 변환
		ProductDTO productDTO = ProductDTO.of(product);

		// 상품 이미지 리스트에서 URL을 추출하여 DTO에 설정
		List<String> imgUrls = product.getProductImgList().stream() // product 엔티티에서 이미지 리스트를 직접 가져옴
				.map(ProductImg::getFileUrl) // 이미지 URL만 추출
				.collect(Collectors.toList());

		// 이미지 URL 리스트를 DTO에 설정
		productDTO.setProductImgUrls(imgUrls);

		return productDTO;
	}

	public List<ProductDTO> getProductsByIds(List<Long> productIds) {
		List<Product> products = productRepository.findAllById(productIds);
		return products.stream()
				.map(ProductDTO::of) // Product 엔티티를 ProductDTO로 변환
				.collect(Collectors.toList());
	}


	// 상품 타입에 따라 상품 목록 조회 (썸네일 이미지를 포함하여 반환)
	public List<ProductDTO> getProductByType(ProductType productType) {
		List<Product> products = productRepository.findByProductType(productType);

		return products.stream()
				.map(product -> {
					ProductDTO productDTO = ProductDTO.of(product);
					// 썸네일 이미지 URL 설정
					String firstImageUrl = getFirstImageUrl(product.getId());
					productDTO.setFirstImageUrl(firstImageUrl);
					return productDTO;
				})
				.collect(Collectors.toList());
	}

	// 상품 수정
	@Transactional(rollbackFor = Exception.class)
	public void updateProduct(Long id, ProductDTO productDTO, String userEmail) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("해당하는 상품을 찾을 수 없습니다. ID: " + id));

		// 작성자와 현재 로그인한 사용자가 동일한지 확인
		if (!product.getCreateBy().equals(userEmail)) {
			throw new IllegalArgumentException("자신이 등록한 상품만 수정할 수 있습니다.");
		}

		product.updateItem(productDTO); // 수정 메서드 호출
		productRepository.save(product); // 수정된 정보 저장
	}

	// 상품 삭제
	@Transactional(rollbackFor = Exception.class)
	public void deleteProduct(Long id, String userEmail) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + id));

		// 작성자와 현재 로그인한 사용자가 동일한지 확인
		if (!product.getCreateBy().equals(userEmail)) {
			throw new IllegalArgumentException("본인이 등록한 상품만 삭제할 수 있습니다.");
		}

		productRepository.deleteById(id); // 상품 삭제
	}

	public List<ProductDTO> searchProducts(String keyword, String category, ProductType productType) {
		List<Product> products;
		switch (category) {
			case "productName":
				products = productRepository.findByProductNameContainingAndProductType(keyword, productType);
				break;
			case "productDetail":
				products = productRepository.findByProductDetailContainingAndProductType(keyword, productType);
				break;
			case "member.email": // 작성자 이메일 검색
				products = productRepository.findByMemberEmailContainingAndProductType(keyword, productType);
				break;
			default:
				throw new IllegalArgumentException("잘못된 검색 카테고리: " + category);
		}

		// 검색된 상품 목록을 DTO로 변환하고, 각 상품의 첫 번째 썸네일 이미지 URL을 설정
		return products.stream().map(product -> {
			ProductDTO productDTO = ProductDTO.of(product);
			String firstImageUrl = product.getProductImgList().stream()
					.filter(ProductImg::isThumbnail) // 썸네일로 지정된 이미지를 찾음
					.map(ProductImg::getFileUrl)
					.findFirst()
					.orElse("/img/default.jpg"); // 썸네일이 없을 경우 기본 이미지 사용
			productDTO.setFirstImageUrl(firstImageUrl); // 썸네일 URL 설정
			return productDTO;
		}).collect(Collectors.toList());
	}

	// 등록된 상품 랜덤으로 가져오기
	public List<ProductDTO> getRandomProducts(int count) {
		// ProductRepository에서 랜덤으로 상품을 가져오기
		List<Product> products = productRepository.findRandomProducts(PageRequest.of(0, count));

		return products.stream()
				.map(product -> {
					ProductDTO productDTO = ProductDTO.of(product);

					// 썸네일 이미지 URL 설정
					String firstImageUrl = product.getProductImgList().stream()
							.filter(ProductImg::isThumbnail) // 썸네일로 지정된 이미지를 찾음
							.map(ProductImg::getFileUrl)
							.findFirst()
							.orElse("/img/default.jpg"); // 썸네일이 없을 경우 기본 이미지 사용
					productDTO.setFirstImageUrl(firstImageUrl);

					return productDTO;
				})
				.collect(Collectors.toList());
	}

}