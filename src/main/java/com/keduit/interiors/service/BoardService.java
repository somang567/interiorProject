package com.keduit.interiors.service;

import com.keduit.interiors.dto.BoardDTO;
import com.keduit.interiors.entity.Board;
import com.keduit.interiors.entity.EmptyNumber;
import com.keduit.interiors.repository.BoardRepository;
import com.keduit.interiors.repository.EmptyNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EmptyNumberRepository emptyNumberRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 엔티티 -> DTO 변환
    public BoardDTO entityToDto(Board board) {
        return new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getAuthor(),
                board.getImageFilename(),
                board.getImagePath(),
                board.getFilename(),
                board.getFilepath(),
                board.getCommentCount(),
                board.getViewCount(),
                board.getRegTime(),
                board.getUpdateTime()
        );
    }

    // DTO -> 엔티티 변환
    public Board dtoToEntity(BoardDTO dto) {
        Board board = new Board();
        board.setId(dto.getId());
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setAuthor(dto.getAuthor());
        board.setImageFilename(dto.getImageFilename());
        board.setImagePath(dto.getImagePath());
        board.setFilename(dto.getFilename());
        board.setFilepath(dto.getFilepath());
        board.setCommentCount(dto.getCommentCount());
        board.setViewCount(dto.getViewCount());
        board.setRegTime(dto.getRegTime());
        board.setUpdateTime(dto.getUpdateTime());
        return board;
    }

    // 게시글 저장 (write)
    public void write(BoardDTO boardDTO, MultipartFile file) throws Exception {
        Board board = dtoToEntity(boardDTO);
        // 빈 번호 확인 및 처리
        Optional<EmptyNumber> emptyNumberOpt = emptyNumberRepository.findFirstByOrderByNumberAsc();
        Long boardId;
        if (emptyNumberOpt.isPresent()) {
            boardId = emptyNumberOpt.get().getNumber();
            emptyNumberRepository.deleteById(boardId); // 사용된 빈 번호 삭제
        } else {
            boardId = boardRepository.findAll().stream()
                    .mapToLong(Board::getId)
                    .max()
                    .orElse(0L) + 1;
        }
        board.setId(boardId);

        // 파일 저장
        saveFile(board, file);
        boardRepository.save(board);
    }

    // 게시글 수정 (update)
    public void update(BoardDTO boardDTO, MultipartFile file, boolean deleteExistingFile) throws Exception {
        Board board = dtoToEntity(boardDTO);
        Board existingBoard = boardRepository.findById(board.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + board.getId()));

        // 새로운 파일이 업로드된 경우 처리
        if (file != null && !file.isEmpty()) {
            updateFile(existingBoard, file);
        }

        existingBoard.setTitle(board.getTitle());
        existingBoard.setContent(board.getContent());

        boardRepository.save(existingBoard);
    }

    // 파일 저장 로직
    private void saveFile(Board board, MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();  // 디렉토리 생성
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
    }

    // 파일 업데이트 메서드
    private void updateFile(Board board, MultipartFile file) throws IOException {
        deleteFile(board);
        saveFile(board, file);
    }

    // 기존 파일 삭제
    public void deleteFile(Board board) {
        if (board.getFilename() != null && !board.getFilename().isEmpty()) {
            String filePath = uploadDir + "/" + board.getFilename();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();  // 파일 삭제
            }
            board.setFilename(null);
            board.setFilepath(null);
        }
    }

    // 게시글 목록 조회
    public Page<BoardDTO> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable).map(this::entityToDto);
    }

    // 게시글 상세 조회
    public BoardDTO boardView(Long id) {
        Optional<Board> boardOpt = boardRepository.findById(id);

        if (boardOpt.isPresent()) {
            Board board = boardOpt.get();
            board.setViewCount(board.getViewCount() + 1); // 조회수 증가
            boardRepository.save(board); // 변경사항 저장
            return entityToDto(board);
        }
        return null;
    }

    // 게시글 삭제
    public void boardDelete(Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            deleteFile(board);
            boardRepository.deleteById(id);

            EmptyNumber emptyNumber = new EmptyNumber();
            emptyNumber.setNumber(id);
            emptyNumberRepository.save(emptyNumber);
        }
    }

    // 게시글 검색 목록 조회
    public Page<BoardDTO> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable).map(this::entityToDto);
    }
}
