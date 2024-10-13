package com.keduit.interiors.controller;

import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.Product;
import com.keduit.interiors.entity.ProductImg;
import com.keduit.interiors.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
	// 상품등록페이지 ( Model에 ProductDTO 를 받아서 Form처리 )
	@GetMapping("/addItem")
	public String addProductForm(ProductDTO productDTO , Model model) {
		model.addAttribute("ProductDTO" , new ProductDTO());

		return "/product/itemForm";
	}
	@PostMapping("/addItem") // POST 요청을 처리할 메서드
	public String addProduct(@ModelAttribute ProductDTO productDTO) {

		// 상품 등록 로직 처리 (예: DB에 저장)
		productService.addProduct(productDTO);

		// 서비스 메서드를 통해 저장 후 ID 반환
		Long productId = productDTO.getId();

		// 상품 종류에 따라 리다이렉트 URL 설정
		String redirectUrl = "/item/productMain"; // 기본 리다이렉트 URL
		if ("타일".equals(productDTO.getProductType())) {
			redirectUrl = "/item/Tail"; // 타일 상품 리스트 페이지
		} else if ("벽지".equals(productDTO.getProductType())) {
			redirectUrl = "/item/Wall"; // 벽지 상품 리스트 페이지
		} else if ("수납".equals(productDTO.getProductType())) {
			redirectUrl = "/item/Stock"; // 수납 상품 리스트 페이지
		} else if ("가구".equals(productDTO.getProductType())) {
			redirectUrl = "/item/furniture"; // 가구 상품 리스트 페이지
		} else if ("바닥재".equals(productDTO.getProductType())) {
			redirectUrl = "/item/floor"; // 바닥재 상품 리스트 페이지
		}
			return "redirect:" + redirectUrl; // 설정한 URL로 리다이렉트
	}

	// 상품찾기 메인 페이지 ( 상품리스트 페이지 가기 전 )
	@GetMapping("/productMain")
	public String productMain() { // 자제 찾기 메인페이지
		return "/product/productMain";
	}

	// 타일 상품리스트 페이지
	@GetMapping("/Tail")
	public String FirstItemPage() {
		return "/product/TailItemList";
	}

	// 벽지 상품리스트 페이지
	@GetMapping("/Wall")
	public String SecondItemPage() {
		return "/product/WallItemList";
	}

	// 수납 상품리스트 페이지
	@GetMapping("/Stock")
	public String ThreeItemPage() {
		return "/product/StockItemList";
	}

	// 가구 상품리스트 페이지
	@GetMapping("/furniture")
	public String FourItemPage() {
		return "/product/FurnitureItemList";
	}

	// 바닥재 상품리스트 페이지
	@GetMapping("/floor")
	public String FiveItemPage() {
		return "/product/FloorItemList";
	}

	// 상품 상세정보 패이지
	@GetMapping("/itemDetail")
	public String detailPage() { // 상세보기 페이지
		return "/product/itemDetail";
	}

}
