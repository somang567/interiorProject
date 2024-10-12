package com.keduit.interiors.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchDTO {
  private int companyId;
  private String companyName;
  private String jobType;
  private String roadAddress;
  private String zipNumber;
  private String contact;

  // 기본 생성자
  public SearchDTO() {}

  // 전체 필드를 사용하는 생성자
  public SearchDTO(int companyId, String companyName, String jobType, String roadAddress, String zipNumber, String contact) {
    this.companyId = companyId;
    this.companyName = companyName;
    this.jobType = jobType;
    this.roadAddress = roadAddress;
    this.zipNumber = zipNumber;
    this.contact = contact;
  }

  // Getter Setter 메서드

  public int getCompanyId() {
    return companyId;
  }

  public void setCompanyId(int companyId) {
    this.companyId = companyId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public String getRoadAddress() {
    return roadAddress;
  }

  public void setRoadAddress(String roadAddress) {
    this.roadAddress = roadAddress;
  }

  public String getZipNumber() {
    return zipNumber;
  }

  public void setZipNumber(String zipNumber) {
    this.zipNumber = zipNumber;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }
}
