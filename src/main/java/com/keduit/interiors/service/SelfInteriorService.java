package com.keduit.interiors.service;

import com.keduit.interiors.dto.SelfInteriorDTO;
import com.keduit.interiors.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SelfInteriorService {

    // 게시글 작성 메서드 (write)
    void write(SelfInteriorDTO selfInteriorDTO, MultipartFile file, Member member) throws IOException;

    // 게시글 조회 메서드 (view)
    SelfInteriorDTO view(Long id);

    // 게시글 목록 조회 메서드 (list)
    Page<SelfInteriorDTO> list(Pageable pageable);

    // 게시글 검색 메서드 - 제목으로 검색
    Page<SelfInteriorDTO> searchByTitle(String searchKeyword, Pageable pageable);

    // 게시글 검색 메서드 - 내용으로 검색
    Page<SelfInteriorDTO> searchByContent(String searchKeyword, Pageable pageable);

    // 게시글 검색 메서드 - 제목 또는 내용으로 검색
    Page<SelfInteriorDTO> searchByTitleOrContent(String searchKeyword, Pageable pageable);

    // 게시글 수정 메서드 (update)
    void update(SelfInteriorDTO selfInteriorDTO, MultipartFile file, boolean deleteExistingFile, Member member) throws IOException, AccessDeniedException;

    // 게시글 삭제 메서드 (delete)
    void delete(Long id, Member member) throws AccessDeniedException;

    void save(SelfInteriorDTO selfInteriorDTO, MultipartFile file, Member member) throws IOException;
}
