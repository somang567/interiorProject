package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Comment;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.MegazineComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MegazineCommentRepository extends JpaRepository<MegazineComment, Long> {
    //select * from comment_table where megazind_id=? order by id desc;
    List<MegazineComment> findAllByMegazineOrderByIdDesc(Megazine megazine);
    Page<MegazineComment> findById(Long id, Pageable pageable);
    List<MegazineComment> findByMegazineMno(Long megazineMno);
}
