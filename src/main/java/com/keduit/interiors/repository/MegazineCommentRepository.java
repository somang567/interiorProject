package com.keduit.interiors.repository;

import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.MegazineComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MegazineCommentRepository extends JpaRepository<MegazineComment, Long> {
    //select * from comment_table where megazind_id=? order by id desc;
    List<MegazineComment> findAllByMegazineOrderByIdDesc(Megazine megazine);
}