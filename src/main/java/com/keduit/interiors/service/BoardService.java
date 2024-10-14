package com.keduit.interiors.service;

import com.keduit.interiors.entity.Board;
import com.keduit.interiors.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public void write(Board board, MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        // 파일이 비어있을 경우에도 게시글을 작성할 수 있도록 로직 수정
        if (file == null || file.isEmpty()) {
            board.setFilename(null); // 파일명 설정
            board.setFilepath(null); // 파일 경로 설정
        } else {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, filename);

            // 디렉토리 확인 및 생성
            File dir = new File(projectPath);
            if (!dir.exists()) {
                dir.mkdirs(); // 디렉토리가 없으면 생성
            }

            // 파일 저장
            file.transferTo(saveFile);

            board.setFilename(filename);
            board.setFilepath("/files/" + filename); // DB에 사진 집어넣기
        }

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

            // 디렉토리 확인 및 생성
            File dir = new File(projectPath);
            if (!dir.exists()) {
                dir.mkdirs(); // 디렉토리가 없으면 생성
            }

            // 파일 저장
            file.transferTo(saveFile);

            // 새로운 파일 정보로 업데이트
            existingBoard.setFilename(filename);
            existingBoard.setFilepath("/files/" + filename); // DB에 새로운 파일 경로 저장
        }
        // 파일이 새로 업로드되지 않았다면 기존 정보 유지 (아무것도 하지 않음)

        boardRepository.save(existingBoard); // 게시글 업데이트
    }



    // 게시글에 파일을 업데이트하는 메서드
    public void updateFile(Board board, MultipartFile file) throws IOException {
        // 기존 파일 삭제 로직 (필요한 경우)
        if (board.getFilepath() != null && !board.getFilepath().isEmpty()) {
            File oldFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static" + board.getFilepath());
            if (oldFile.exists()) {
                oldFile.delete(); // 이전 파일 삭제
            }
        }

        // 새 파일 정보 설정
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, filename);

        // 디렉토리 확인 및 생성
        File dir = new File(projectPath);
        if (!dir.exists()) {
            dir.mkdirs(); // 디렉토리가 없으면 생성
        }

        // 파일 저장
        file.transferTo(saveFile);

        // board 엔티티에 파일명과 파일 경로 설정
        board.setFilename(filename);
        board.setFilepath("/files/" + filename);

        // board 엔티티를 데이터베이스에 업데이트
        boardRepository.save(board); // 게시글 업데이트
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
