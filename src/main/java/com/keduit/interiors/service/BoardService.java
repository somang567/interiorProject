package com.keduit.interiors.service;

import com.keduit.interiors.entity.Board;
import com.keduit.interiors.entity.EmptyNumber;
import com.keduit.interiors.repository.BoardRepository;
import com.keduit.interiors.repository.EmptyNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;  // 추가
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

    @Autowired
    private EmptyNumberRepository emptyNumberRepository;

    // 설정 파일에서 업로드 경로를 주입받습니다.
    @Value("${file.upload-dir}")
    private String uploadDir;

    public void write(Board board, MultipartFile file) throws Exception {
        // 1. 빈 번호가 있는지 확인
        Optional<EmptyNumber> emptyNumberOpt = emptyNumberRepository.findFirstByOrderByNumberAsc();

        Long boardId;
        if (emptyNumberOpt.isPresent()) {
            // 2. 빈 번호가 있으면 그 번호를 사용
            boardId = emptyNumberOpt.get().getNumber();
            emptyNumberRepository.deleteById(boardId); // 사용된 빈 번호 삭제
        } else {
            // 3. 빈 번호가 없으면 기존 게시글 중 최대 번호 + 1 사용
            boardId = boardRepository.findAll().stream()
                    .mapToLong(Board::getId)
                    .max()
                    .orElse(0L) + 1;
        }

        // 4. 게시글 ID 설정
        board.setId(boardId);

        // 파일 저장 로직 (경로 수정: uploadDir 사용)
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();  // 디렉토리 없으면 생성
        }

        if (file == null || file.isEmpty()) {
            board.setFilename(null);
            board.setFilepath(null);
        } else {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(dir, filename);
            file.transferTo(saveFile);

            board.setFilename(filename);
            board.setFilepath("/files/" + filename);
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
            String filePath = uploadDir + "/" + board.getFilename();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();  // 파일 삭제
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
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();  // 디렉토리 없으면 생성
        }

        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(dir, filename);

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

            // 빈 번호 테이블에 삭제된 게시글 번호 추가
            EmptyNumber emptyNumber = new EmptyNumber();
            emptyNumber.setNumber(id);
            emptyNumberRepository.save(emptyNumber);
        }
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
}
