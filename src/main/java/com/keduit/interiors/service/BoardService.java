package com.keduit.interiors.service;

import com.keduit.interiors.dto.BoardDTO;
import com.keduit.interiors.entity.Board;
import com.keduit.interiors.entity.EmptyNumber;
import com.keduit.interiors.entity.Member;
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

    // 게시글 저장 메서드 (write)
    public void write(BoardDTO boardDTO, MultipartFile file, Member member) throws Exception {
        // DTO를 엔티티로 변환
        Board board = dtoToEntity(boardDTO);
        board.setAuthor(member); // 작성자를 Member로 설정

        // 빈 번호 확인 및 처리
        Optional<EmptyNumber> emptyNumberOpt = emptyNumberRepository.findFirstByOrderByNumberAsc();
        Long boardId;
        if (emptyNumberOpt.isPresent()) {
            boardId = emptyNumberOpt.get().getNumber();
            emptyNumberRepository.deleteById(boardId);
        } else {
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

    // 게시글 조회 메서드 (boardView)
    public BoardDTO boardView(Long id) {
        Optional<Board> boardOpt = boardRepository.findById(id);

        if (boardOpt.isPresent()) {
            Board board = boardOpt.get();
            board.setViewCount(board.getViewCount() + 1); // 조회수 증가
            boardRepository.save(board); // 변경사항 저장
            return entityToDto(board); // DTO로 변환하여 반환
        }
        return null;
    }

    // 게시글 삭제 메서드 (boardDelete)
    public void boardDelete(Long id) throws IllegalAccessException {
        // 게시글 조회
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 게시글 ID: " + id));

        // 게시글 삭제
        boardRepository.deleteById(id);
    }

    // 게시글 목록 조회 메서드 (boardList)
    public Page<BoardDTO> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(this::entityToDto);
    }

    // 게시글 검색 메서드 - 제목으로 검색
    public Page<BoardDTO> boardSearchByTitle(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable)
                .map(this::entityToDto);
    }

    // 게시글 검색 메서드 - 내용으로 검색
    public Page<BoardDTO> boardSearchByContent(String searchKeyword, Pageable pageable) {
        return boardRepository.findByContentContaining(searchKeyword, pageable)
                .map(this::entityToDto);
    }

    // 게시글 검색 메서드 - 제목 또는 내용으로 검색
    public Page<BoardDTO> boardSearchByTitleOrContent(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContainingOrContentContaining(searchKeyword, searchKeyword, pageable)
                .map(this::entityToDto);
    }

    // 엔티티 -> DTO 변환 메서드
    public BoardDTO entityToDto(Board board) {
        return new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getAuthor().getId(),  // 작성자의 ID
                board.getAuthor().getName(), // 작성자의 이름
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
        Board board = new Board();
        board.setId(dto.getId());
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        // 작성자는 따로 설정
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

    // 게시글 수정 메서드 (update)
    public void update(BoardDTO boardDTO, MultipartFile file, boolean deleteExistingFile, Member member) throws Exception {
        // DTO를 엔티티로 변환
        Board board = dtoToEntity(boardDTO);
        // 기존 게시글 조회
        Board existingBoard = boardRepository.findById(board.getId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 게시글 ID: " + board.getId()));

        // 작성자 확인
        if (!existingBoard.getAuthor().equals(member)) {
            throw new IllegalAccessException("해당 게시글을 수정할 권한이 없습니다.");
        }

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
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
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

    // 기존 파일 삭제 메서드
    public void deleteFile(Board board) {
        if (board.getFilename() != null && !board.getFilename().isEmpty()) {
            String filePath = uploadDir + "/" + board.getFilename();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            board.setFilename(null);
            board.setFilepath(null);
        }
    }
}
