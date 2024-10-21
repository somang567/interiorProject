package com.keduit.interiors.controller;

import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.dto.MegazineCommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/megacomment")
public class MegazineCommentController {

    @PostMapping("/save")
    public @ResponseBody String save(@ModelAttribute MegazineCommentDTO megazineCommentDTO) {

        System.out.println("commentDTO = " + megazineCommentDTO);
        return "요청 성공";
    }


}
