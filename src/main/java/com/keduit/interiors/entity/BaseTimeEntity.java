package com.keduit.interiors.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.SimpleFormatter;

// Auditing을 적용하기 위해 @EntityListeners 애노테이션을 추가
@EntityListeners(value={AuditingEntityListener.class})
// 공통 매핑 정보가 필요할 때 사용하는 애노테이션, 부모 클래스를 상속받는 자식 클래스에 매핑정보만 제공.
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {

  @CreatedDate
  @Column(updatable = false)
  private String regTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));

  @LastModifiedDate
  private String updateDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
}
