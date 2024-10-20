package com.keduit.interiors.controller;

import com.keduit.interiors.constant.ProductType;
import com.keduit.interiors.dto.ProductDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.service.MemberService;
import com.keduit.interiors.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	private final MemberService memberService;

	// 메인 페이지
	@GetMapping("/productMain")
	public String productMain(Model model, Principal principal) {
		boolean isAdmin = isAdmin(principal);
		// isAdmin 값 콘솔 출력
		System.out.println("Is Admin: " + isAdmin);

		model.addAttribute("isAdmin", isAdmin(principal));  // 관리자 여부 추가
		return "/product/productMain";
	}

	// 상품 등록 페이지로 이동
	@GetMapping("/addItem")
	public String addProductForm(Model model, Principal principal) {

		// 관리자 여부를 모델에 추가
		boolean isAdmin = isAdmin(principal);
		model.addAttribute("isAdmin", isAdmin);  // 관리자 여부 추가

		if (!isAdmin) {
			return "redirect:/item/productMain";  // 관리자가 아닌 경우 메인 페이지로 리다이렉트
		}

		model.addAttribute("productDTO", new ProductDTO());
		return "/product/itemForm";  // 상품 등록 페이지로 이동
	}

	@PostMapping("/addItem")
	public String addProduct(@ModelAttribute ProductDTO productDTO,
	                         @RequestParam("multipartFileList") List<MultipartFile> files,
	                         Principal principal, Model model) {
		String email = principal.getName();
		Member member = memberService.findByEmail(email);

		productDTO.setMemberId(member.getId());
		// 관리자 권한 확인
		if (!isAdmin(principal)) {
			return "redirect:/item/productMain";  // 권한이 없는 경우 리다이렉트
		}

		// 상품 등록 후 등록된 상품 ID 반환
		Long productId = null;
		try {
			productId = productService.addProduct(productDTO, files);
		} catch (Exception e) {
			// 등록 실패 시 로그 남기기
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품 등록 중 오류가 발생했습니다.");
			return "/product/itemForm";  // 에러 발생 시 등록 폼으로 돌아감
		}

		// 등록된 상품의 정보를 Model에 추가
		productDTO = productService.getProductById(productId);
		model.addAttribute("productDTO", productDTO);

		// 상품 타입에 따라 각 페이지로 리다이렉트
		return redirectByProductType(productDTO.getProductType());
	}


	@GetMapping("/product/{product_id}")
	public String getProductDetail(@PathVariable("product_id") Long productId, Principal principal, Model model) {
		ProductDTO productDTO = productService.getProductById(productId);
		model.addAttribute("productDTOList", productDTO);

		if (principal != null) {
			// 현재 로그인한 사용자의 이메일을 이용해 멤버를 조회합니다.
			String email = principal.getName();
			Member member = memberService.findByEmail(email);
			if (member != null && member.getId().equals(productDTO.getMemberId())) {
				model.addAttribute("isAdmin", true);
			} else {
				model.addAttribute("isAdmin", false);
			}
		} else {
			model.addAttribute("isAdmin", false);
		}

		return "product/itemDetail"; // 상세 페이지로 이동
	}

	// 수정 폼으로 이동 (GET 요청)
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
	public String updateProduct(@PathVariable("productId") Long productId, ProductDTO productDTO, Principal principal, Model model) {
		ProductDTO existingProduct = productService.getProductById(productId);

		if (!existingProduct.getCreateBy().equals(principal.getName())) {
			return "redirect:/item/productMain";  // 작성자와 로그인한 사용자가 다를 경우 리다이렉트
		}

		productService.updateProduct(productId, productDTO, principal.getName());
		return redirectByProductType(productDTO.getProductType());// 수정 후 상세 페이지로 리다이렉트
	}

	// 상품 삭제 처리
	@PostMapping("/deleteItem/{id}")
	public String deleteProduct(@PathVariable Long id, Principal principal, Model model) {
		ProductDTO productDTO = productService.getProductById(id);

		if (!productDTO.getCreateBy().equals(principal.getName())) {
			return "redirect:/item/productMain";  // 작성자와 로그인한 사용자가 다를 경우 리다이렉트
		}

		productService.deleteProduct(id, principal.getName());

		// 상품 타입에 따라 리다이렉트 URL 설정
		return redirectByProductType(productDTO.getProductType());
	}

	// 각 상품 리스트 페이지
	@GetMapping("/wall")
	public String wallItemList(Model model, Principal principal) {
		List<ProductDTO> wallProducts = productService.getProductByType(ProductType.WALL);
		model.addAttribute("productList", wallProducts);
		model.addAttribute("isAdmin", isAdmin(principal));  // 관리자 여부 추가
		return "/product/wallItemList";
	}

	@GetMapping("/tile")
	public String tileItemList(Model model, Principal principal) {
		List<ProductDTO> tileProducts = productService.getProductByType(ProductType.TILE);
		model.addAttribute("productList", tileProducts);
		model.addAttribute("isAdmin", isAdmin(principal));  // 관리자 여부 추가
		return "/product/tileItemList";
	}

	@GetMapping("/stock")
	public String stockItemList(Model model, Principal principal) {
		List<ProductDTO> stockProducts = productService.getProductByType(ProductType.STOCK);
		model.addAttribute("productList", stockProducts);
		model.addAttribute("isAdmin", isAdmin(principal));  // 관리자 여부 추가
		return "/product/stockItemList";
	}

	@GetMapping("/furniture")
	public String furnitureItemList(Model model, Principal principal) {
		List<ProductDTO> furnitureProducts = productService.getProductByType(ProductType.FURNITURE);
		model.addAttribute("productList", furnitureProducts);
		model.addAttribute("isAdmin", isAdmin(principal));  // 관리자 여부 추가
		return "/product/furnitureItemList";
	}

	@GetMapping("/floor")
	public String floorItemList(Model model, Principal principal) {
		List<ProductDTO> floorProducts = productService.getProductByType(ProductType.FLOOR);
		model.addAttribute("productList", floorProducts);
		model.addAttribute("isAdmin", isAdmin(principal));  // 관리자 여부 추가
		return "/product/floorItemList";
	}
	private boolean isAdmin(Principal principal) {
		if (principal == null) {
			return false;
		}

		// SecurityContextHolder에서 Authentication 객체를 얻고 "ROLE_ADMIN"으로 권한 확인
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = authentication.getAuthorities()
				.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

		System.out.println("현재 사용자의 권한: " + authentication.getAuthorities());
		System.out.println("관리자 여부: " + isAdmin);

		return isAdmin;
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
