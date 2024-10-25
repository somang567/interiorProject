package com.keduit.interiors.service;

import com.keduit.interiors.dto.CompanypromotionDTO;
import com.keduit.interiors.entity.Companypromotion;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.CompanypromotionRepository;
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
public class CompanypromotionService {

    @Autowired
    private CompanypromotionRepository companypromotionRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 게시글 저장 메서드 (write)
    public void write(CompanypromotionDTO companypromotionDTO, MultipartFile file, Member member) throws Exception {
        // DTO를 엔티티로 변환
        Companypromotion companypromotion = dtoToEntity(companypromotionDTO);
        companypromotion.setAuthor(member); // 작성자를 Member로 설정

        // 파일 저장
        saveFile(companypromotion, file);

        // 게시글 저장
        companypromotionRepository.save(companypromotion);
    }

    // 게시글 조회 메서드 (view)
    public CompanypromotionDTO view(Long id) {
        Optional<Companypromotion> companypromotionOpt = companypromotionRepository.findById(id);

        if (companypromotionOpt.isPresent()) {
            Companypromotion companypromotion = companypromotionOpt.get();
            return entityToDto(companypromotion); // DTO로 변환하여 반환
        }
        return null;
    }

    // 게시글 삭제 메서드 (delete)
    public void delete(Long id) throws IllegalAccessException {
        // 게시글 조회
        Companypromotion companypromotion = companypromotionRepository.getById(id);

        // 게시글 삭제
        companypromotionRepository.deleteById(id);
    }

    // 게시글 목록 조회 메서드 (list)
    public Page<CompanypromotionDTO> list(Pageable pageable) {
        return companypromotionRepository.findAll(pageable)
                .map(this::entityToDto);
    }

    // 게시글 검색 메서드 - 제목으로 검색
    public Page<CompanypromotionDTO> searchByTitle(String searchKeyword, Pageable pageable) {
        return companypromotionRepository.findByTitleContaining(searchKeyword, pageable)
                .map(this::entityToDto);
    }

    // 게시글 검색 메서드 - 내용으로 검색
    public Page<CompanypromotionDTO> searchByContent(String searchKeyword, Pageable pageable) {
        return companypromotionRepository.findByContentContaining(searchKeyword, pageable)
                .map(this::entityToDto);
    }

    // 게시글 검색 메서드 - 제목 또는 내용으로 검색
    public Page<CompanypromotionDTO> searchByTitleOrContent(String searchKeyword, Pageable pageable) {
        return companypromotionRepository.findByTitleContainingOrContentContaining(searchKeyword, searchKeyword, pageable)
                .map(this::entityToDto);
    }

    // 엔티티 -> DTO 변환 메서드
    public CompanypromotionDTO entityToDto(Companypromotion companypromotion) {
        return new CompanypromotionDTO(
                companypromotion.getId(),
                companypromotion.getTitle(),
                companypromotion.getContent(),
                companypromotion.getAuthor() != null ? companypromotion.getAuthor().getId() : null,
                companypromotion.getAuthor() != null ? companypromotion.getAuthor().getName() : null,
                companypromotion.getFilename(),
                companypromotion.getFilepath(),
                companypromotion.getCommentCount(),
                companypromotion.getViewCount(),
                companypromotion.getRegTime(),
                companypromotion.getUpdateTime()
        );
    }

    // DTO -> 엔티티 변환 메서드
    public Companypromotion dtoToEntity(CompanypromotionDTO dto) {
        Companypromotion companypromotion = new Companypromotion();
        companypromotion.setId(dto.getId());
        companypromotion.setTitle(dto.getTitle());
        companypromotion.setContent(dto.getContent());
        companypromotion.setFilename(dto.getFilename());
        companypromotion.setFilepath(dto.getFilepath());
        companypromotion.setCommentCount(dto.getCommentCount());
        companypromotion.setViewCount(dto.getViewCount());
        companypromotion.setRegTime(dto.getRegTime());
        companypromotion.setUpdateTime(dto.getUpdateTime());
        return companypromotion;
    }

    // 게시글 수정 메서드 (update)
    public void update(CompanypromotionDTO companypromotionDTO, MultipartFile file, boolean deleteExistingFile, Member member) throws Exception {
        // 기존 게시글 조회
        Companypromotion existingCompanypromotion = companypromotionRepository.getById(companypromotionDTO.getId());

        // 작성자 확인
        if (!existingCompanypromotion.getAuthor().equals(member)) {
            throw new IllegalAccessException("해당 게시글을 수정할 권한이 없습니다.");
        }

        // 제목과 내용 업데이트
        existingCompanypromotion.setTitle(companypromotionDTO.getTitle());
        existingCompanypromotion.setContent(companypromotionDTO.getContent());

        // 파일 처리
        if (deleteExistingFile) {
            deleteFile(existingCompanypromotion);
        }

        if (file != null && !file.isEmpty()) {
            saveFile(existingCompanypromotion, file);
        }

        // 게시글 저장
        companypromotionRepository.save(existingCompanypromotion);
    }

    // 파일 저장 로직 메서드
    private void saveFile(Companypromotion companypromotion, MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (file == null || file.isEmpty()) {
            companypromotion.setFilename(null);
            companypromotion.setFilepath(null);
        } else {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(dir, filename);
            file.transferTo(saveFile);

            companypromotion.setFilename(filename);
            companypromotion.setFilepath("/files/" + filename);
        }
    }

    // 기존 파일 삭제 메서드
    public void deleteFile(Companypromotion companypromotion) {
        if (companypromotion.getFilename() != null && !companypromotion.getFilename().isEmpty()) {
            String filePath = uploadDir + "/" + companypromotion.getFilename();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            companypromotion.setFilename(null);
            companypromotion.setFilepath(null);
        }
    }

    // 조회수 증가 메서드
    public void incrementViewCount(Long id) {
        companypromotionRepository.findById(id).ifPresent(companypromotion -> {
            companypromotion.setViewCount(companypromotion.getViewCount() + 1);
            companypromotionRepository.save(companypromotion);
        });
    }
}
