package com.keduit.interiors.service;

import com.keduit.interiors.entity.Board;
import com.keduit.interiors.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository; // 객체를 생성 - 스프링 부트에서 제공하는 Autowired를 사용하면 스프링이 알아서 읽어와서 자동으로 주입을 해준다

    public void write(Board board) {
        boardRepository.save(board);
    }

    public List<Board> boardList() {
        return boardRepository.findAll();
    }
}
