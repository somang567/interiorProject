package com.keduit.interiors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {
	@GetMapping("/productMain")
	public String productMain(){ // 자제 찾기 메인페이지
		return "/product/productMain";
	}

	@GetMapping("/itemList")
	public String itemPage(){
		return "/product/itemList";
	}

	@GetMapping("/itemDetail")
	public String detailPage(){ // 상세보기 페이지
		return "/product/itemDetail";
	}
}
