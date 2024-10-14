
package com.keduit.interiors.service;

import com.keduit.interiors.dto.ItemImgDTO;
import com.keduit.interiors.dto.MegazineDTO;
import com.keduit.interiors.entity.ItemImg;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.ItemImgRepository;
import com.keduit.interiors.repository.MegazineRepository;
import com.keduit.interiors.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
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

  @Autowired
  private ItemImgService itemImgService;

  @Autowired
  private ItemImgRepository itemImgRepository;

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


  //제공하신 코드는 Java의 Spring Framework에서 상품(Item)과 관련된 정보를 저장하는 서비스 메서드
  //all 커밋이 되거나 롤백이 되어야 함.
  //상품이 몇번인지 Long으로 등록
  public Long saveItem(MegazineDTO megazineDTO, List<MultipartFile> itemImgFileList) throws Exception{

    //상품 등록
    //Item 엔티티 가져와서
    Megazine megazine = megazineDTO.createItem();
    //모델 매퍼 사용
    megazineRepository.save(megazine);  //리포지토리는 엔티티를 줘야 하기 때문에

    //이미지 등록
    for( int i = 0; i < itemImgFileList.size(); i++){
      ItemImg itemImg = new ItemImg();
      itemImg.setMegazine(megazine);  //조인 걸려 있어서 item을 통째로 가져옴.
      //대표 이미지 세팅함.
      if(i==0){
        itemImg.setRegImgYn("Y");
      }else{
        itemImg.setRegImgYn("N");
      }
      System.out.println("itemImg, itemImgFileList.get(i)" + itemImg +"----===="+ itemImgFileList.get(i));
      itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
    }

    return megazine.getMno();  //Long 타입 리턴하는 메서드임.
  }

  //readOnly = true : 상품 데이터를 읽기만 하므로 JPA에게 더티 체킹(변경 감지->업데이트)를 하지 않도록 설정.
  // 성능상 이점이 발생 (더 빨라짐)
  @Transactional(readOnly = true)
  public MegazineDTO getItemDtl(Long megazineId){
    List<ItemImg> itemImgList = itemImgRepository.findByMegazine_MnoOrderByIdAsc(megazineId);
    List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
    for (ItemImg itemImg : itemImgList){
      ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);
      itemImgDTOList.add(itemImgDTO);
    }

    //위에 이미지 읽었으니 아이템 읽어야즤
    Megazine megazine = megazineRepository.findById(megazineId)
        .orElseThrow(EntityNotFoundException::new);
    MegazineDTO megazineDTO = MegazineDTO.of(megazine); // 매핑해줌 아이템DTO로 변경해줌
    //앞에서 이미지 담아서 주려고
    megazineDTO.setItemImgDTOList(itemImgDTOList);

    return megazineDTO;
  }

  //MultipartFile 화면에서 받아옴
  public Long updateItem(MegazineDTO megazineDTO,
                         List<MultipartFile> itemImgFileList) throws Exception{
    //상품 수정
    //findById 애가 옵셔널을 들고 있음.
    System.out.println("---- updateItem. itemDto ====>" + megazineDTO);
    Megazine megazine = megazineRepository.findById(megazineDTO.getMno())
        .orElseThrow(EntityNotFoundException::new);

    megazine.updateItem(megazineDTO);

    List<Long> itemImgIds = megazineDTO.getItemImgIds(); //이미지들의 아이디가 있어야 업데이트를 하거나 삭제함

    //이미지 등록
    for(int i=0; i < itemImgFileList.size(); i++){
      System.out.println("****" + i + "-----" + itemImgIds.get(i) + "-----" + itemImgFileList.get(i));
      itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
    }
    return megazine.getMno();
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
