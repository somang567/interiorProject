package com.keduit.interiors.service;

import com.keduit.interiors.entity.Board;
import com.keduit.interiors.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // 추가
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 설정 파일에서 업로드 경로를 주입받습니다. 없을 경우 기본값으로 "uploads" 사용
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public void write(Board board, MultipartFile file) throws Exception {
        // 파일 저장 경로를 외부 디렉토리로 변경
        String projectPath = System.getProperty("user.dir") + "/" + uploadDir;

        // 디렉토리 확인 및 생성
        File dir = new File(projectPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일이 비어있을 경우에도 게시글을 작성할 수 있도록 로직 수정
        if (file == null || file.isEmpty()) {
            board.setFilename(null);
            board.setFilepath(null);
        } else {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(dir, filename);

            // 파일 저장
            file.transferTo(saveFile);

            board.setFilename(filename);
            board.setFilepath("/files/" + filename); // DB에 파일 경로 저장
        }

        boardRepository.save(board);
    }

    public void update(Board board, MultipartFile file, boolean deleteExistingFile) throws Exception {
        Board existingBoard = boardRepository.findById(board.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + board.getId()));

        existingBoard.setTitle(board.getTitle());
        existingBoard.setContent(board.getContent());

        // 기존 파일 삭제 체크박스가 선택된 경우
        if (deleteExistingFile) {
            deleteFile(existingBoard);
        }

        // 새로운 파일이 업로드된 경우
        if (file != null && !file.isEmpty()) {
            updateFile(existingBoard, file);
        }

        // 게시글 업데이트
        boardRepository.save(existingBoard);
    }

    // 기존 파일 삭제 메서드
    public void deleteFile(Board board) {
        if (board.getFilename() != null && !board.getFilename().isEmpty()) {
            String filePath = System.getProperty("user.dir") + "/" + uploadDir + "/" + board.getFilename();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete(); // 파일 삭제
            }
            // 데이터베이스에서 파일 정보 삭제
            board.setFilename(null);
            board.setFilepath(null);
        }
    }

    // 파일 업데이트 메서드
    public void updateFile(Board board, MultipartFile file) throws IOException {
        // 기존 파일 삭제
        deleteFile(board);

        // 새 파일 저장
        String projectPath = System.getProperty("user.dir") + "/" + uploadDir;
        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, filename);

        // 디렉토리 확인 및 생성
        File dir = new File(projectPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일 저장
        file.transferTo(saveFile);

        // 새로운 파일 정보 설정
        board.setFilename(filename);
        board.setFilepath("/files/" + filename);
    }

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Board boardView(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        return board.orElse(null);
    }

    public void boardDelete(Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            // 게시글 삭제 전에 파일 삭제
            deleteFile(board);
            boardRepository.deleteById(id);
        }
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
}
