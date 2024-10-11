package com.keduit.interiors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {
	// 상품찾기 메인 페이지 ( 상품리스트 페이지 가기 전 )
	@GetMapping("/productMain")
	public String productMain(){ // 자제 찾기 메인페이지
		return "/product/productMain";
	}

	// 타일 상품리스트 페이지
	@GetMapping("/itemList")
	public String FirstItemPage(){
		return "/product/TailItemList";
	}

	// 벽지 상품리스트 페이지
	@GetMapping("/itemList")
	public String SecondItemPage(){
		return "/product/WallItemList";
	}

	// 수납 상품리스트 페이지
	@GetMapping("/itemList")
	public String ThreeItemPage(){
		return "/product/StockItemList";
	}

	// 가구 상품리스트 페이지
	@GetMapping("/itemList")
	public String FourItemPage(){
		return "/product/FurnitureItemList";
	}

	// 바닥재 상품리스트 페이지
	@GetMapping("/itemList")
	public String FiveItemPage(){
		return "/product/FloorItemList";
	}

	// 상품 상세정보 패이지
	@GetMapping("/itemDetail")
	public String detailPage(){ // 상세보기 페이지
		return "/product/itemDetail";
	}
}
