//package com.keduit.interiors.controller;
//
//import com.keduit.interiors.dto.CommentDTO;
//import com.keduit.interiors.dto.MegazineCommentDTO;
//import com.keduit.interiors.dto.MemberDTO;
//import com.keduit.interiors.entity.MegazineComment;
//import com.keduit.interiors.entity.Member;
//import com.keduit.interiors.repository.MemberRepository;
//import com.keduit.interiors.service.CommentService;
//import com.keduit.interiors.service.MegazineCommentService;
//import com.keduit.interiors.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/megacomment")
//public class MegazineCommentController {
//
//    private final MegazineCommentService megazineCommentService;
//    private final MemberService memberService;
//    private final MemberRepository memberRepository;
//
//    @PostMapping("/save")
//    public ResponseEntity save(@ModelAttribute MegazineCommentDTO megazineCommentDTO, Long megazineId) {
//
//        megazineCommentDTO.setId(megazineId);
//        //megazineCommentDTO.getId(memberRepository.findAllById());
//        if (megazineCommentDTO.getId() == null) {
//            System.out.println("Error: ID is null");
//            return ResponseEntity.badRequest().body("ID must not be null");
//        }
//
//        System.out.println("megazineCommentDTO =============> " + megazineCommentDTO);
//
//        //Long saveResult = megazineCommentService.saveComment(megazineCommentDTO);
//        if (saveResult != null) {
//
//            //댓글 작성 성공 시 댓글 목록을 가져와서 리턴
//            //댓글 목록: 해당 게시글의 댓글 전체
//            //댓글 목록 가져올 때 게시글을 기준으로 가져와야 함.
//            //List<MegazineCommentDTO> megazineCommentDTOList = megazineCommentService.findAll(megazineCommentDTO.getMegazineId());
//            return new ResponseEntity<>(megazineCommentDTOList, HttpStatus.OK);
//        }else {
//            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
//        }
//
//
//    }
//
//
//    @GetMapping("/api")
//    @ResponseBody
//    public Page<MegazineComment> test(@PageableDefault(sort="id", value=5, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(name = "id") Long id) {
//        Page<MegazineComment> comments = megazineCommentService.findComments(id, pageable);
//        return comments;
//    }
//
//
//
//
//}
