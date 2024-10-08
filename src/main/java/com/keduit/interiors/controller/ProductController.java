package com.keduit.interiors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {
	@GetMapping("/productMain")
	public String productMain(){
		return "/product/productMain";
	}
	@PostMapping("/main")
	public String detailPage(){
		return "";
	}
}