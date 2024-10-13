package com.keduit.interiors.controller;

import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.Product;
import com.keduit.interiors.entity.ProductImg;
import com.keduit.interiors.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
