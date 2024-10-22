package com.keduit.interiors.controller;

import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.MemberService;
import com.keduit.interiors.service.ProductScrapService;
import com.keduit.interiors.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final ProductScrapService productScrapService;
	private final MemberService memberService;


	// 상품 목록에서 메인 페이지로 이동
	@GetMapping("/productMain")
	public String productMainRedirect() {
		return "/product/productMain";
	}

	// 상품 등록 페이지로 이동
	@GetMapping("/addItem")
	public String addProductForm(Model model, Principal principal) {
		boolean isAdmin = isAdmin(principal);
		model.addAttribute("isAdmin", isAdmin);

		if (!isAdmin) {
			return "redirect:/item/productMain";
		}

		model.addAttribute("productDTO", new ProductDTO());
		return "/product/itemForm";
	}

	// 상품 등록 처리
	@PostMapping("/addItem")
	public String addProduct(@ModelAttribute ProductDTO productDTO,
	                         @RequestParam("multipartFileList") List<MultipartFile> files,
	                         Principal principal, Model model) {
		// 로그인한 사용자 정보를 가져와서 상품에 연결
		String email = principal.getName();
		Member member = memberService.findByEmail(email);

		productDTO.setMemberId(member.getId());

		// 관리자 권한 확인
		if (!isAdmin(principal)) {
			return "redirect:/item/productMain";
		}

		try {
			productService.addProduct(productDTO, files);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품 등록 중 오류가 발생했습니다.");
			return "/product/itemForm";
		}

		// 등록된 상품의 타입에 따라 해당 상품 페이지로 리다이렉트
		return redirectByProductType(productDTO.getProductType());
	}

	// 상품 수정 페이지로 이동
	@GetMapping("/updateItem/{productId}")
	public String showUpdateForm(@PathVariable("productId") Long productId, Model model, Principal principal) {
		ProductDTO productDTO = productService.getProductById(productId);

		if (!productDTO.getCreateBy().equals(principal.getName())) {
			return "redirect:/item/productMain";  // 작성자와 로그인한 사용자가 다를 경우 리다이렉트
		}

		model.addAttribute("productDTO", productDTO);
		return "product/itemEditForm";  // 수정 폼으로 이동
	}

	// 상품 수정 처리
	@PostMapping("/updateItem/{productId}")
	public String updateProduct(@PathVariable("productId") Long productId,
	                            @ModelAttribute ProductDTO productDTO,
	                            Principal principal, Model model) {
		ProductDTO existingProduct = productService.getProductById(productId);

		if (!existingProduct.getCreateBy().equals(principal.getName())) {
			return "redirect:/item/productMain";  // 작성자와 로그인한 사용자가 다를 경우 리다이렉트
		}

		productService.updateProduct(productId, productDTO, principal.getName());
		// 수정 후 상품 타입에 따라 해당 페이지로 리다이렉트
		return redirectByProductType(productDTO.getProductType());
	}

	// 상품 삭제 처리
	@PostMapping("/deleteItem/{productId}")
	public String deleteProduct(@PathVariable Long productId, Principal principal, Model model) {
		ProductDTO productDTO = productService.getProductById(productId);

		if (!productDTO.getCreateBy().equals(principal.getName())) {
			return "redirect:/item/productMain";  // 작성자와 로그인한 사용자가 다를 경우 리다이렉트
		}

		productService.deleteProduct(productId, principal.getName());

		// 삭제 후 상품 타입에 따라 해당 페이지로 리다이렉트
		return redirectByProductType(productDTO.getProductType());
	}

	// 상품 상세보기
	@GetMapping("/product/{product_id}")
	public String getProductDetail(@PathVariable("product_id") Long productId, Model model, Principal principal) {
		ProductDTO productDTO = productService.getProductById(productId);
		model.addAttribute("productDTO", productDTO);

		// 스크랩 정보 추가
		setScrapDataForUser(model, principal);

		boolean isAdmin = isAdmin(principal);
		model.addAttribute("isAdmin", isAdmin);

		return "/product/itemDetail";
	}

	// 각 상품 페이지들 (타일, 벽, 바닥, 가구, 재고)
	@GetMapping("/wall")
	public String wallItemList(Model model, Principal principal) {
		List<ProductDTO> wallProducts = productService.getProductByType(ProductType.WALL);
		setScrapDataForUser(model, principal);
		model.addAttribute("productList", wallProducts);
		model.addAttribute("isAdmin", isAdmin(principal));
		return "/product/wallItemList";
	}

	@GetMapping("/tile")
	public String tileItemList(Model model, Principal principal) {
		List<ProductDTO> tileProducts = productService.getProductByType(ProductType.TILE);
		setScrapDataForUser(model, principal);
		model.addAttribute("productList", tileProducts);
		model.addAttribute("isAdmin", isAdmin(principal));
		return "/product/tileItemList";
	}

	@GetMapping("/floor")
	public String floorItemList(Model model, Principal principal) {
		List<ProductDTO> floorProducts = productService.getProductByType(ProductType.FLOOR);
		setScrapDataForUser(model, principal);
		model.addAttribute("productList", floorProducts);
		model.addAttribute("isAdmin", isAdmin(principal));
		return "/product/floorItemList";
	}

	@GetMapping("/furniture")
	public String furnitureItemList(Model model, Principal principal) {
		List<ProductDTO> furnitureProducts = productService.getProductByType(ProductType.FURNITURE);
		setScrapDataForUser(model, principal);
		model.addAttribute("productList", furnitureProducts);
		model.addAttribute("isAdmin", isAdmin(principal));
		return "/product/furnitureItemList";
	}

	@GetMapping("/stock")
	public String stockItemList(Model model, Principal principal) {
		List<ProductDTO> stockProducts = productService.getProductByType(ProductType.STOCK);
		setScrapDataForUser(model, principal);
		model.addAttribute("productList", stockProducts);
		model.addAttribute("isAdmin", isAdmin(principal));
		return "/product/stockItemList";
	}

	// 관리자 확인 메서드
	private boolean isAdmin(Principal principal) {
		if (principal == null) {
			return false;
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getAuthorities()
				.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
	}

	// 사용자의 스크랩 정보 설정 메서드
	private void setScrapDataForUser(Model model, Principal principal) {
		if (principal != null) {
			Member member = memberService.findByEmail(principal.getName());
			List<Long> scrapProductIds = productScrapService.getScrapProductIdsForUser(member.getId());
			model.addAttribute("scrapProductIds", scrapProductIds);
		}
	}

	// 상품 타입별로 리다이렉트하는 메서드
	private String redirectByProductType(ProductType productType) {
		switch (productType) {
			case TILE:
				return "redirect:/item/tile";
			case WALL:
				return "redirect:/item/wall";
			case STOCK:
				return "redirect:/item/stock";
			case FURNITURE:
				return "redirect:/item/furniture";
			case FLOOR:
				return "redirect:/item/floor";
			default:
				return "redirect:/item/productMain";
		}
	}
}
