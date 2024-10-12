
package com.keduit.interiors.service;

import com.keduit.interiors.dto.MegazineDTO;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.MegazineRepository;
import com.keduit.interiors.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MegazineService {

  @Autowired
  private MegazineRepository megazineRepository;

  @Autowired
  private MemberRepository memberRepository;


  @Transactional(readOnly = true) //데이터 베이스 성능 최적화를 위함.
  //CartDetailDTO 응? 여기서 리스트를 뽑아온다? 얘가 몬데 씌벌?
  //아니 얘가 사용자를 이메일로 인식을 하면
  // 게시글을 가져올 때도 Member와 조인을 맺어서 그 멤버 아이디로 가져와야 하는 것 아닌가?
  public List<MegazineDTO> getBoardList() {
    //findByMemberId 얘가 지금 findByMemberId(Long member_id)라는데

    List<MegazineDTO> boardDTOList = new ArrayList<>();
    List<Megazine> megazineList = megazineRepository.findAll();

    //해당 게시물이 존재하지 않을 경우 null 반환.
    if (megazineList.size() == 0 || megazineList.isEmpty()) {
      System.out.println("게시물이 존재하지 않습니다.");
      return null;
    }
//Board -> BoardList
//Board 타입을 BoardDTO에 mapper로 형변환 해서 넣어줘서 뿌려야 함.
    for (Megazine megazineLists : megazineList){
      MegazineDTO megazineDTO = MegazineDTO.of(megazineLists);
      boardDTOList.add(megazineDTO);
    }
    return boardDTOList;
  }

  @Transactional(readOnly = true)
  public Page<Megazine> getListItemPage(Pageable pageable){
    return megazineRepository.findAll(pageable); //모든 아이템을 가져온다.
    //.getListItemPage(pageable);
  }


  // 포맷팅을 위해 LocalDateTime의 now()메소드를 사용해 현재 시간 구한다.
  LocalDateTime now = LocalDateTime.now();

  // 지정된 패턴을 사용해 포맷터를 만든다
  DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("a hh:mm");



  //여기였던 것 같다!!!
  public Long register(MegazineDTO megazineDTO, Principal principal){
    Member member = memberRepository.findByEmail(principal.getName());
    System.out.println("----------------register: " + megazineDTO.createBoard());
    Megazine megazine = megazineDTO.createBoard();
    megazine.setMember(member);
    System.out.println("board -----> " + megazine);
    Megazine savemegazine = megazineRepository.save(megazine);
    System.out.println("saveboard------>" + savemegazine);
    return savemegazine.getMno();
  }


  //-------------------------------------------------------------

  public MegazineService(MegazineRepository megazineRepository) {
    this.megazineRepository = megazineRepository;
  }

  public List<Megazine> getList() {
    return megazineRepository.findAll();
  }

  public Optional<Megazine> getBoard(Long bno) {
    return megazineRepository.findById(bno);
  }

  public void save(Megazine megazine) {
    megazineRepository.save(megazine);
  }

  public void delete(Long bno) {
    megazineRepository.deleteById(bno);
  }









}
