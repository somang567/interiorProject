package com.keduit.interiors.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

//모오든 엔티티에게 상속받도록 할 것임.
//부모 것은 다 자식꺼임
//JPA가 엔티티의 생명주기 이벤트에 대해 특정 클래스(여기서는 AuditingEntityListener)를 리스너로 사용하도록 지정
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{

  @CreatedBy
  @Column(updatable = false)
  private String createBy;  //작성자

  @LastModifiedBy
  private String modifiedBy;  //수정자
}

