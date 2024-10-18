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

    // 파일 업로드 경로 설정
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 게시글 검색 메서드 - 제목으로 검색
    public Page<BoardDTO> boardSearchByTitle(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable).map(this::entityToDto);
    }

    // 게시글 검색 메서드 - 내용으로 검색
    public Page<BoardDTO> boardSearchByContent(String searchKeyword, Pageable pageable) {
        return boardRepository.findByContentContaining(searchKeyword, pageable).map(this::entityToDto);
    }

    // 게시글 검색 메서드 - 제목 또는 내용으로 검색
    public Page<BoardDTO> boardSearchByTitleOrContent(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContainingOrContentContaining(searchKeyword, searchKeyword, pageable).map(this::entityToDto);
    }

    // 엔티티 -> DTO 변환 메서드
    public BoardDTO entityToDto(Board board) {
        // Board 엔티티를 BoardDTO로 변환하여 반환
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

    // DTO -> 엔티티 변환 메서드
    public Board dtoToEntity(BoardDTO dto) {
        // BoardDTO를 Board 엔티티로 변환하여 반환
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

    // 게시글 저장 메서드 (write)
    public void write(BoardDTO boardDTO, MultipartFile file) throws Exception {
        // DTO를 엔티티로 변환
        Board board = dtoToEntity(boardDTO);
        // 빈 번호 확인 및 처리
        Optional<EmptyNumber> emptyNumberOpt = emptyNumberRepository.findFirstByOrderByNumberAsc();
        Long boardId;
        if (emptyNumberOpt.isPresent()) {
            // 비어 있는 번호가 있는 경우 해당 번호 사용
            boardId = emptyNumberOpt.get().getNumber();
            emptyNumberRepository.deleteById(boardId); // 사용된 빈 번호 삭제
        } else {
            // 비어 있는 번호가 없는 경우 최대 번호에 1을 더하여 새로운 번호 생성
            boardId = boardRepository.findAll().stream()
                    .mapToLong(Board::getId)
                    .max()
                    .orElse(0L) + 1;
        }
        board.setId(boardId);

        // 파일 저장
        saveFile(board, file);
        // 게시글 저장
        boardRepository.save(board);
    }

    // 게시글 수정 메서드 (update)
    public void update(BoardDTO boardDTO, MultipartFile file, boolean deleteExistingFile) throws Exception {
        // DTO를 엔티티로 변환
        Board board = dtoToEntity(boardDTO);
        // 기존 게시글 조회
        Board existingBoard = boardRepository.findById(board.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + board.getId()));

        // 새로운 파일이 업로드된 경우 처리
        if (file != null && !file.isEmpty()) {
            updateFile(existingBoard, file);
        }

        // 제목과 내용 업데이트
        existingBoard.setTitle(board.getTitle());
        existingBoard.setContent(board.getContent());

        // 게시글 저장
        boardRepository.save(existingBoard);
    }

    // 파일 저장 로직 메서드
    private void saveFile(Board board, MultipartFile file) throws IOException {
        // 파일 저장 디렉토리 생성
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();  // 디렉토리 생성
        }

        // 파일이 없는 경우 파일 정보 초기화
        if (file == null || file.isEmpty()) {
            board.setFilename(null);
            board.setFilepath(null);
        } else {
            // 파일이 있는 경우 파일 저장 처리
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
        // 기존 파일 삭제 후 새로운 파일 저장
        deleteFile(board);
        saveFile(board, file);
    }

    // 기존 파일 삭제 메서드
    public void deleteFile(Board board) {
        // 기존 파일이 있는 경우 삭제 처리
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

    // 게시글 목록 조회 메서드
    public Page<BoardDTO> boardList(Pageable pageable) {
        // 페이지네이션을 사용하여 게시글 목록 조회 후 DTO로 변환하여 반환
        return boardRepository.findAll(pageable).map(this::entityToDto);
    }

    // 게시글 상세 조회 메서드
    public BoardDTO boardView(Long id) {
        // 게시글 조회
        Optional<Board> boardOpt = boardRepository.findById(id);

        if (boardOpt.isPresent()) {
            Board board = boardOpt.get();
            board.setViewCount(board.getViewCount() + 1); // 조회수 증가
            boardRepository.save(board); // 변경사항 저장
            return entityToDto(board); // DTO로 변환하여 반환
        }
        return null;
    }

    // 게시글 삭제 메서드
    public void boardDelete(Long id) {
        // 게시글 조회
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            deleteFile(board); // 파일 삭제
            boardRepository.deleteById(id); // 게시글 삭제

            // 사용된 게시글 번호를 빈 번호로 저장
            EmptyNumber emptyNumber = new EmptyNumber();
            emptyNumber.setNumber(id);
            emptyNumberRepository.save(emptyNumber);
        }
    }

    // 게시글 검색 목록 조회 메서드
    public Page<BoardDTO> boardSearchList(String searchKeyword, Pageable pageable) {
        // 검색어를 포함하는 게시글 목록 조회 후 DTO로 변환하여 반환
        return boardRepository.findByTitleContaining(searchKeyword, pageable).map(this::entityToDto);
    }
}
