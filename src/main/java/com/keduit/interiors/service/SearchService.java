package com.keduit.interiors.service;

import com.keduit.interiors.dto.SearchDTO;
import com.keduit.interiors.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

// Service : 비즈니스 로직을 구현, 레포지토리와 연결되어 데이터 처리, 컨트롤러와 레포지토리 간의 중재 역할
@Service
public class SearchService {

  @Autowired
  //private SearchRepository searchRepository;    // 객체 생성, 레포지토리의 내용 주입 가능
  private JdbcTemplate jdbcTemplate;              // JdbcTemplate 객체 주입

  // 모든 검색 결과를 가져오는 메소드
  public List<SearchDTO> getSearchResults(String city, String district) {
    String sql = "SELECT * FROM search WHERE road_address LIKE ? AND road_address LIKE ?";
    String cityPattern = "%" + city + "%";      // 예: 부산
    String districtPattern = "%" + district + "%";  // 예: 강서구

    return jdbcTemplate.query(sql, new Object[]{cityPattern, districtPattern}, (rs, rowNum) -> new SearchDTO(
        rs.getInt("company_id"),
        rs.getString("company_name"),
        rs.getString("job_type"),
        rs.getString("road_address"),
        rs.getString("zip_number"),
        rs.getString("contact")
    ));
  }


}
