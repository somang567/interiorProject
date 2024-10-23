package com.keduit.interiors.controller;

import com.keduit.interiors.dto.CsCommentDTO;
import com.keduit.interiors.service.CsCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/csComment")
@RequiredArgsConstructor
public class CsCommentController {

	private final CsCommentService csCommentService;

	// 댓글 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<?> addComment(@RequestBody CsCommentDTO csCommentDTO, Principal principal) {
		// 로그인한 사용자의 이메일로 댓글 추가
		csCommentService.addComment(csCommentDTO, principal.getName());
		return ResponseEntity.ok().build();
	}

	// 댓글 목록 조회
	@GetMapping("/list/{csId}")
	public ResponseEntity<List<CsCommentDTO>> getComments(@PathVariable Long csId) {
		List<CsCommentDTO> comments = csCommentService.getCommentsByCsId(csId);
		return ResponseEntity.ok(comments);
	}
}
