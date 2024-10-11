package com.keduit.interiors.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

// entity <-- (repository, service) --> controller : 엔티티와 컨트롤러를 레포지토리&서비스가 연결해줌.
// Entity : 테이블 매핑. 데이터베이스 테이블의 구조를 나타내며, 해당 데이터를 어떻게 저장할지를 결정함
@Entity
@Data
public class Search {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "company_id")      // 열 이름 매핑
  private long company_id;          // 순번(1,2,3,4 ...)

  @Column(name = "company_name", nullable = false)  // null 허용X
  private String company_name;      // 상호명

  @Column(name = "job_type")
  private String job_type;          // 업종

  @Column(name = "road_address", nullable = false)  // null 허용X
  private String road_address;      // 도로명 주소

  @Column(name = "zip_number")
  private String zip_number;        // 우편번호

  @Column(name = "contact", nullable = false)       // null 허용X
  private String contact;           // 전화번호
}
