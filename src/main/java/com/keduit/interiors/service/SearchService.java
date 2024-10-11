package com.keduit.interiors.service;

import com.keduit.interiors.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Service : 비즈니스 로직을 구현, 레포지토리와 연결되어 데이터 처리, 컨트롤러와 레포지토리 간의 중재 역할
@Service
public class SearchService {

  @Autowired
  private SearchRepository searchRepository;    // 객체 생성, 레포지토리의 내용 주입 가능
}
