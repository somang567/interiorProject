//package com.keduit.interiors.entity;
//
//import lombok.Getter;
//import org.springframework.data.annotation.CreatedBy;
//import org.springframework.data.annotation.LastModifiedBy;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.Column;
//import javax.persistence.EntityListeners;
//import javax.persistence.MappedSuperclass;
//
//@EntityListeners(value={AuditingEntityListener.class})
//// 공통 매핑 정보가 필요할 때 사용하는 애노테이션, 부모 클래스를 상속받는 자식 클래스에 매핑정보만 제공.
//@MappedSuperclass
//@Getter
//public abstract class BaseEntity extends BaseTimeEntity{
//
//  @CreatedBy
//  @Column(updatable = false)
//  private String createBy;
//
//  @LastModifiedBy
//  private String modifiedBy;
//}
