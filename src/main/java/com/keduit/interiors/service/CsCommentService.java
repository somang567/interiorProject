package com.keduit.interiors.service;

import com.keduit.interiors.dto.CommentDTO;
import com.keduit.interiors.dto.CsCommentDTO;
import com.keduit.interiors.entity.CSEntity;
import com.keduit.interiors.entity.CsComment;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.CommentRepository;
import com.keduit.interiors.repository.CsCommentRepository;
import com.keduit.interiors.repository.CsRepository;
import com.keduit.interiors.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsCommentService {
	private final CsCommentRepository csCommentRepository;
	private final CsRepository csRepository;
	private final MemberRepository memberRepository;

	public void addComment(CsCommentDTO commentDTO, String memberEmail) {
		CSEntity csEntity = csRepository.findById(commentDTO.getCsId())
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다."));
		Member member = memberRepository.findByEmail(memberEmail);

		CsComment comment = new CsComment();
		comment.setContent(commentDTO.getContent());
		comment.setCsEntity(csEntity);
		comment.setMember(member);
		comment.setRegTime(LocalDateTime.now());

		csCommentRepository.save(comment);
	}

	public List<CsCommentDTO> getCommentsByCsId(Long csId) {
		List<CsComment> comments = csCommentRepository.findByCsEntityId(csId);
		return comments.stream().map(comment -> {
			CsCommentDTO dto = new CsCommentDTO();
			dto.setId(comment.getId());
			dto.setCsId(comment.getCsEntity().getId());
			dto.setContent(comment.getContent());
			dto.setMemberEmail(comment.getMember().getEmail());
			dto.setRegTime(comment.getRegTime());
			return dto;
		}).collect(Collectors.toList());
	}
}
