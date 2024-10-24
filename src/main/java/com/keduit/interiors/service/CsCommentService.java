package com.keduit.interiors.service;

import com.keduit.interiors.dto.CsCommentDTO;
import com.keduit.interiors.entity.CsComment;
import com.keduit.interiors.entity.CSEntity;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.CsCommentRepository;
import com.keduit.interiors.repository.CsRepository;
import com.keduit.interiors.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CsCommentService {

	private final CsCommentRepository csCommentRepository;
	private final CsRepository csRepository;
	private final MemberRepository memberRepository;

	// 댓글 추가
	public void addComment(CsCommentDTO csCommentDTO, String userEmail) {
		CSEntity csEntity = csRepository.findById(csCommentDTO.getCsId())
				.orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
		Member member = memberRepository.findByEmail(userEmail);

		CsComment comment = new CsComment();
		comment.setContent(csCommentDTO.getContent());
		comment.setCsEntity(csEntity);
		comment.setMember(member);
		comment.setRegTime(csCommentDTO.getRegTime());

		csCommentRepository.save(comment);
	}

	// 댓글 조회 (수정을 위한)
	public CsCommentDTO getCommentById(Long id) {
		CsComment comment = csCommentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
		return CsCommentDTO.of(comment);
	}

	// 게시물에 속한 모든 댓글 조회
	public List<CsCommentDTO> getCommentsByCsId(Long csId) {
		List<CsComment> comments = csCommentRepository.findByCsEntityId(csId);
		return comments.stream()
				.map(CsCommentDTO::of)
				.collect(Collectors.toList());
	}

	// 댓글 수정
	public void updateComment(Long id, CsCommentDTO csCommentDTO, String userEmail) {
		CsComment comment = csCommentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
		if (!comment.getMember().getEmail().equals(userEmail)) {
			throw new IllegalArgumentException("댓글 작성자만 수정할 수 있습니다.");
		}
		comment.setContent(csCommentDTO.getContent());
		csCommentRepository.save(comment);
	}

	// 댓글 삭제
	public void deleteComment(Long id, String userEmail) {
		CsComment comment = csCommentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
		if (!comment.getMember().getEmail().equals(userEmail)) {
			throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
		}
		csCommentRepository.delete(comment);
	}
}
