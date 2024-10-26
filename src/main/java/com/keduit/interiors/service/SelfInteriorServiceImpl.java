package com.keduit.interiors.service;

import com.keduit.interiors.constant.Role;
import com.keduit.interiors.dto.SelfInteriorDTO;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.entity.SelfInterior;
import com.keduit.interiors.repository.SelfInteriorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SelfInteriorServiceImpl implements SelfInteriorService {

    private static final Logger logger = LoggerFactory.getLogger(SelfInteriorServiceImpl.class);

    @Autowired
    private SelfInteriorRepository selfInteriorRepository;

    @Autowired
    private MemberService memberService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    @Transactional
    public void write(SelfInteriorDTO selfInteriorDTO, MultipartFile file, Member member) throws IOException {
        SelfInterior selfInterior = dtoToEntity(selfInteriorDTO);
        selfInterior.setAuthor(member);

        saveFile(selfInterior, file);

        selfInteriorRepository.save(selfInterior);
        logger.info("SelfInterior {} created by user {}", selfInterior.getId(), member.getEmail());
    }

    @Override
    @Transactional
    public SelfInteriorDTO view(Long id) {
        Optional<SelfInterior> selfInteriorOpt = selfInteriorRepository.findById(id);

        if (selfInteriorOpt.isPresent()) {
            SelfInterior selfInterior = selfInteriorOpt.get();
            selfInterior.setViewCount(selfInterior.getViewCount() + 1);
            selfInteriorRepository.save(selfInterior);
            return entityToDto(selfInterior);
        }
        return null;
    }

    @Override
    public Page<SelfInteriorDTO> list(Pageable pageable) {
        return selfInteriorRepository.findAll(pageable)
                .map(this::entityToDto);
    }

    @Override
    public Page<SelfInteriorDTO> searchByTitle(String searchKeyword, Pageable pageable) {
        return selfInteriorRepository.findByTitleContaining(searchKeyword, pageable)
                .map(this::entityToDto);
    }

    @Override
    public Page<SelfInteriorDTO> searchByContent(String searchKeyword, Pageable pageable) {
        return selfInteriorRepository.findByContentContaining(searchKeyword, pageable)
                .map(this::entityToDto);
    }

    @Override
    public Page<SelfInteriorDTO> searchByTitleOrContent(String searchKeyword, Pageable pageable) {
        return selfInteriorRepository.findByTitleContainingOrContentContaining(searchKeyword, searchKeyword, pageable)
                .map(this::entityToDto);
    }

    @Override
    @Transactional
    public void update(SelfInteriorDTO selfInteriorDTO, MultipartFile file, boolean deleteExistingFile, Member member) throws IOException, AccessDeniedException {
        SelfInterior existingSelfInterior = selfInteriorRepository.getById(selfInteriorDTO.getId());

        boolean isAdmin = member.getRole() == Role.ADMIN;

        logger.info("User {} attempting to update SelfInterior {}", member.getEmail(), selfInteriorDTO.getId());
        logger.info("Is Admin: {}", isAdmin);

        if (existingSelfInterior.getAuthor().equals(member) || isAdmin) {
            if (file != null && !file.isEmpty()) {
                updateFile(existingSelfInterior, file);
            }

            if (deleteExistingFile) {
                deleteFile(existingSelfInterior);
            }

            existingSelfInterior.setTitle(selfInteriorDTO.getTitle());
            existingSelfInterior.setContent(selfInteriorDTO.getContent());

            selfInteriorRepository.save(existingSelfInterior);
            logger.info("SelfInterior {} updated by user {}", existingSelfInterior.getId(), member.getEmail());
        } else {
            logger.warn("User {} attempted to update SelfInterior {} without permission", member.getEmail(), existingSelfInterior.getId());
            throw new AccessDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }
    }

    @Override
    @Transactional
    public void delete(Long id, Member member) throws AccessDeniedException {
        SelfInterior selfInterior = selfInteriorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 게시글 ID: " + id));

        boolean isAdmin = member.getRole() == Role.ADMIN;

        logger.info("User {} attempting to delete SelfInterior {}", member.getEmail(), id);
        logger.info("Is Admin: {}", isAdmin);

        if (selfInterior.getAuthor().equals(member) || isAdmin) {
            selfInteriorRepository.deleteById(id);
            logger.info("SelfInterior {} deleted by user {}", id, member.getEmail());
        } else {
            logger.warn("User {} attempted to delete SelfInterior {} without permission", member.getEmail(), id);
            throw new AccessDeniedException("해당 게시글을 삭제할 권한이 없습니다.");
        }
    }

    @Override
    @Transactional
    public void save(SelfInteriorDTO selfInteriorDTO, MultipartFile file, Member member) throws IOException {
        SelfInterior selfInterior = dtoToEntity(selfInteriorDTO);
        selfInterior.setAuthor(member);

        saveFile(selfInterior, file);

        selfInteriorRepository.save(selfInterior);
        logger.info("SelfInterior {} created by user {}", selfInterior.getId(), member.getEmail());
    }

    private void saveFile(SelfInterior selfInterior, MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (file == null || file.isEmpty()) {
            selfInterior.setFilename(null);
            selfInterior.setFilepath(null);
        } else {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(dir, filename);
            file.transferTo(saveFile);

            selfInterior.setFilename(filename);
            selfInterior.setFilepath("/files/" + filename);
        }
    }

    private void updateFile(SelfInterior selfInterior, MultipartFile file) throws IOException {
        deleteFile(selfInterior);
        saveFile(selfInterior, file);
    }

    private void deleteFile(SelfInterior selfInterior) {
        if (selfInterior.getFilename() != null && !selfInterior.getFilename().isEmpty()) {
            String filePath = uploadDir + "/" + selfInterior.getFilename();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
                logger.info("File {} deleted from server.", selfInterior.getFilename());
            }
            selfInterior.setFilename(null);
            selfInterior.setFilepath(null);
        }
    }

    // 엔티티 -> DTO 변환 메서드
    public SelfInteriorDTO entityToDto(SelfInterior selfInterior) {
        return new SelfInteriorDTO(
                selfInterior.getId(),
                selfInterior.getTitle(),
                selfInterior.getContent(),
                selfInterior.getAuthor().getId(),
                selfInterior.getAuthor().getName(),
                selfInterior.getFilename(),
                selfInterior.getFilepath(),
                selfInterior.getCommentCount(),
                selfInterior.getViewCount(),
                selfInterior.getRegTime(),
                selfInterior.getUpdateTime()
        );
    }

    // DTO -> 엔티티 변환 메서드
    public SelfInterior dtoToEntity(SelfInteriorDTO dto) {
        SelfInterior selfInterior = new SelfInterior();
        selfInterior.setId(dto.getId());
        selfInterior.setTitle(dto.getTitle());
        selfInterior.setContent(dto.getContent());
        selfInterior.setFilename(dto.getFilename());
        selfInterior.setFilepath(dto.getFilepath());
        selfInterior.setCommentCount(dto.getCommentCount());
        selfInterior.setViewCount(dto.getViewCount());
        selfInterior.setRegTime(dto.getRegTime());
        selfInterior.setUpdateTime(dto.getUpdateTime());
        return selfInterior;
    }
}
