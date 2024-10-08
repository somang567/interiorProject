package com.keduit.interiors.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {

    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "boards/boardwrite";
    }

    @PostMapping("/board/writedo")
    public String boardWritePro(String title, String content) {

        System.out.println("제목 : " + title);
        System.out.println("내용 : " + content);
        return "";
    }
}
