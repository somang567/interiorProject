package com.keduit.interiors.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class EmptyNumber {

    @Id
    private Long number;  // 삭제된 번호를 관리해줄 필드

}
