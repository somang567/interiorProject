package com.keduit.interiors.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//@EntityListeners : Auditing을 적용하기 위해 에노테이션 추가.
//{} 2개 이상일 때 중괄호 써줌
@EntityListeners(value ={AuditingEntityListener.class})
// 공통 매핑 정보가 필요할 때 사용하는 애노테이션, 부모 클래스를 상속받는 자식 클래스에 매핑 정보만 제공.
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {

  @CreatedDate
  @Column(updatable = false)  //생성된 날짜를 넣어주고 업데이트 불가
  private LocalDateTime regTime;

  @LastModifiedDate
  private LocalDateTime updateTime;
}

