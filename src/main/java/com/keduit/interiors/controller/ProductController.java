package com.keduit.interiors.controller;

import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.dto.ProductImgDTO;
import com.keduit.interiors.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping("/productMain")
	public String ProductMain() {
		return "/product/productMain";
	}

	// 상품 등록 페이지로 이동
	@GetMapping("/addItem")
	public String addProductForm(Model model) {
		model.addAttribute("productDTO", new ProductDTO());
		return "/product/itemForm"; // 상품 등록 템플릿으로 이동
	}

	// 상품 등록 처리
	@PostMapping("/addItem")
	public String addProduct(@ModelAttribute ProductDTO productDTO,
	                         @RequestParam("multipartFileList") List<MultipartFile> files,
	                         Model model) {
		Long productId = productService.addProduct(productDTO, files);
		String firstImageUrl = productService.getFirstImageUrl(productId);
		model.addAttribute("firstImageUrl", firstImageUrl);

		// 상품 타입에 따라 리다이렉트 URL 설정
		String redirectUrl = "/item/productMain";
		switch (productDTO.getProductType()) {
			case TILE:
				redirectUrl = "/item/tile";
				break;
			case WALL:
				redirectUrl = "/item/wall";
				break;
			case STOCK:
				redirectUrl = "/item/stock";
				break;
			case FURNITURE:
				redirectUrl = "/item/furniture";
				break;
			case FLOOR:
				redirectUrl = "/item/floor";
				break;
		}

		return "redirect:" + redirectUrl; // 해당 타입별 페이지로 리다이렉트
	}

	@GetMapping("/product/{productId}")
	public String getProductDetail(@PathVariable("productId") Long productId, Model model) {
		// productId에 해당하는 상품 정보를 조회
		ProductDTO productDTO = productService.getProductById(productId);

		// 조회한 상품 정보를 모델에 담아 상세 페이지로 전달
		model.addAttribute("productDTOList", productDTO);

		// 상세 페이지로 이동
		return "product/itemDetail";
	}


	// 상품 수정 페이지 이동
	@GetMapping("/updateItem/{id}")
	public String updateProductForm(@PathVariable Long id, Model model) {
		ProductDTO productDTO = productService.getProductById(id);
		model.addAttribute("productDTO", productDTO);
		return "/product/itemEditForm"; // 수정 템플릿으로 이동
	}

	// 상품 수정 처리
	@PostMapping("/updateItem/{id}")
	public String updateProduct(@PathVariable Long id, @ModelAttribute ProductDTO productDTO) {
		productService.updateProduct(id, productDTO);
		return "redirect:/item/productMain"; // 수정 후 메인 페이지로 리다이렉트
	}

	// 상품 삭제 처리
	@PostMapping("/deleteItem/{id}")
	public String deleteProduct(@PathVariable Long id) {
		ProductDTO productDTO = productService.getProductById(id);
		ProductType productType = productDTO.getProductType();

		productService.deleteProduct(id);

		// 상품 타입에 따라 리다이렉트 URL 설정
		String redirectUrl = "/item/productMain";
		switch (productType) {
			case TILE:
				redirectUrl = "/item/tile";
				break;
			case WALL:
				redirectUrl = "/item/wall";
				break;
			case STOCK:
				redirectUrl = "/item/stock";
				break;
			case FURNITURE:
				redirectUrl = "/item/furniture";
				break;
			case FLOOR:
				redirectUrl = "/item/floor";
				break;
		}
		return "redirect:" + redirectUrl; // 해당 타입별 페이지로 리다이렉트
	}

	// 벽지 상품 리스트 페이지
	@GetMapping("/wall")
	public String wallItemList(Model model) {
		List<ProductDTO> wallProducts = productService.getProductByType(ProductType.WALL);
		model.addAttribute("productList", wallProducts);
		return "/product/wallItemList";
	}

	// 타일 상품 리스트 페이지
	@GetMapping("/tile")
	public String tileItemList(Model model) {
		List<ProductDTO> tileProducts = productService.getProductByType(ProductType.TILE);
		model.addAttribute("productList", tileProducts);
		return "/product/tileItemList";
	}

	// 수납 상품 리스트 페이지
	@GetMapping("/stock")
	public String stockItemList(Model model) {
		List<ProductDTO> stockProducts = productService.getProductByType(ProductType.STOCK);
		model.addAttribute("productList", stockProducts);
		return "/product/stockItemList";
	}

	// 가구 상품 리스트 페이지
	@GetMapping("/furniture")
	public String furnitureItemList(Model model) {
		List<ProductDTO> furnitureProducts = productService.getProductByType(ProductType.FURNITURE);
		model.addAttribute("productList", furnitureProducts);
		return "/product/furnitureItemList";
	}

	// 바닥재 상품 리스트 페이지
	@GetMapping("/floor")
	public String floorItemList(Model model) {
		List<ProductDTO> floorProducts = productService.getProductByType(ProductType.FLOOR);
		model.addAttribute("productList", floorProducts);
		return "/product/floorItemList";
	}
}
