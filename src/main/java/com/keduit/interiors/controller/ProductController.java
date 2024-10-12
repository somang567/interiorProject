package com.keduit.interiors.controller;

import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.Product;
import com.keduit.interiors.entity.ProductImg;
import com.keduit.interiors.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/item")
public class ProductController {
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
	@GetMapping("/addItem")
	public String addItemPage(){
		return "/product/itemForm";
	}
	@PostMapping("/addItem")
	public String addProduct(ProductDTO productDTO, List<MultipartFile> images) {
		Product product = new Product();
		// productDTO에서 product에 값 설정 (예: modelMapper 사용)

		List<ProductImg> productImages = new ArrayList<>();
		for (MultipartFile image : images) {
			ProductImg productImg = new ProductImg();
			// 이미지 파일과 관련된 필드 설정
			productImages.add(productImg);
		}

		return "redirect:/item/productMain"; // 상품 등록 후 이동할 페이지
	}
}
