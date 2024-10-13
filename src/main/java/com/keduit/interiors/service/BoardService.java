package com.keduit.interiors.service;

import com.keduit.interiors.entity.Board;
import com.keduit.interiors.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public void write(Board board, MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, filename);

        file.transferTo(saveFile);

        board.setFilename(filename);
        board.setFilepath("/files/" + filename); // DB에 사진 집어넣기

        boardRepository.save(board);
    }

    public void update(Board board, MultipartFile file) throws Exception {
        Board existingBoard = boardRepository.findById(board.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + board.getId()));

        existingBoard.setTitle(board.getTitle());
        existingBoard.setContent(board.getContent());

        // 파일이 새로 업로드된 경우
        if (file != null && !file.isEmpty()) {
            // 파일 처리 로직
            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, filename);
            file.transferTo(saveFile);

            existingBoard.setFilename(filename);
            existingBoard.setFilepath("/files/" + filename); // DB에 새로운 파일 경로 저장
        }
        // 파일이 없으면 기존 filename을 그대로 유지
        boardRepository.save(existingBoard);
    }

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Board boardView(Long id) {
        return boardRepository.findById(id).get();
    }

    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
}
