package com.keduit.interiors.service;

import com.keduit.interiors.dto.BoardDTO;
import com.keduit.interiors.dto.ItemImgDTO;
import com.keduit.interiors.dto.ItemSearchDTO;
import com.keduit.interiors.dto.MegazineDTO;
import com.keduit.interiors.entity.Board;
import com.keduit.interiors.entity.ItemImg;
import com.keduit.interiors.entity.Megazine;
import com.keduit.interiors.entity.Member;
import com.keduit.interiors.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
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

    private final MegazineRepository megazineRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final MegazineCommentRepository megazineCommentRepository;
    private final MegazineScrapRepository megazineScrapRepository;


    @Transactional(readOnly = true) //데이터 베이스 성능 최적화를 위함.
    //CartDetailDTO 응? 여기서 리스트를 뽑아온다? 얘가 몬데 씌벌?
    //아니 얘가 사용자를 이메일로 인식을 하면
    // 게시글을 가져올 때도 Member와 조인을 맺어서 그 멤버 아이디로 가져와야 하는 것 아닌가?
    public List<MegazineDTO> getMegazineList() {
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
        for (Megazine megazineLists : megazineList) {
            MegazineDTO megazineDTO = MegazineDTO.of(megazineLists);
            boardDTOList.add(megazineDTO);
        }
        return boardDTOList;
    }

    //전체 게시글 수 세는 메서드
    public long countTotalMagazines() {
        return megazineRepository.count();
    }

    public long countTotalComments() {
        return megazineCommentRepository.count();
    }

    //전체 게시글 수 세는 메서드
    public long countTotalScraps() {
        return megazineScrapRepository.count();
    }

    @Transactional(readOnly = true)
    public Page<Megazine> getListItemPage(Pageable pageable) {
//        Member member = memberRepository.findByEmail(principal.getName());
//        String Username = member.getName();
//        megazineDTO.setUser(Username);
        return megazineRepository.findAll(pageable); //모든 아이템을 가져온다.
    }

    // 포맷팅을 위해 LocalDateTime의 now()메소드를 사용해 현재 시간 구한다.
    LocalDateTime now = LocalDateTime.now();

    // 지정된 패턴을 사용해 포맷터를 만든다
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("a hh:mm");


    //여기였던 것 같다!!!
    public Long register(MegazineDTO megazineDTO, Principal principal, MultipartFile itemImgFile) throws Exception {
        Member member = memberRepository.findByEmail(principal.getName());
        //  파일 업로드하고, oriImgName,imgName,imgUrl를 받아서 megazineDTO에 넣어줌.
        //FileService 에서 저 3개를 리턴하게 만들어.
        String originalFileName = itemImgFile.getOriginalFilename();
        String imgName = "";  //경로를 가져오아서
        String imgUrl = "";

        //특정 조건에 따라 이미지를 서버에 저장하고 경로를 설정하는 로직. 파일 업로드
        //String 과 관련해서 강력하게 가지고 있는 메서드?
        //originalFileName 이 널이거나 빈 문자열인지 확인
        if (!StringUtils.isEmpty(originalFileName)) {
            //itemImgLocation 이친구가 uploadPathpath임, 파일 위치, 오리지날 파일 이름, 바이트 배열(이미지 파일이니까)
            imgName = fileService.uploadFile(itemImgLocation, originalFileName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;  //cONFIGURE 파일에서 선언되어 있음.
        }


        //상품 이미지 정보 저장
        //상품 이미지 등록 --> 이 부분들은 필요가 없음
        megazineDTO.setImgName(imgName);
        megazineDTO.setOriImgName(originalFileName);
        megazineDTO.setImageUrl(imgUrl);

        Megazine megazine = megazineDTO.createItem();
        System.out.println("----------------register: " + megazine);

        megazine.setMember(member);
        System.out.println("board -----> " + megazine);

        Megazine savemegazine = megazineRepository.save(megazine);
        System.out.println("saveboard------>" + savemegazine);
        return savemegazine.getMno();
    }


    //제공하신 코드는 Java의 Spring Framework에서 상품(Item)과 관련된 정보를 저장하는 서비스 메서드
    //all 커밋이 되거나 롤백이 되어야 함.
    //상품이 몇번인지 Long으로 등록ㄴ

    public Long saveItem(MegazineDTO megazineDTO, MultipartFile itemImgFile, Principal principal) throws Exception {
        
        System.out.println("saveItem 메서드ㄴ ----------------------------------->");

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
        String imgUrl = "/images/item/" + imgName;

        megazineDTO.setImgName(imgName);
        megazineDTO.setOriImgName(oriImgName);
        megazineDTO.setImageUrl(imgUrl);
        //현재 로그인 한 유저
        //이메일 하나 읽어와서 사용자의 이름 가져오기
        Member member = memberRepository.findByEmail(principal.getName());
        String Username = member.getName();
        megazineDTO.setUser(Username);

        File directory = new File("C:\\Users\\user\\Desktop\\fileUploads\\item");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Megazine megazine = megazineDTO.createItem();   //매핑
        megazineRepository.save(megazine);  //리포지토리는 엔티티를 줘야 하기 때문에

        return megazine.getMno();  //Long 타입 리턴하는 메서드임.



        //이미지 등록
    /*
    for( int i = 0; i < 1; i++){
      itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
    }
    */

    /*   for( int i = 0; i < itemImgFileList.size(); i++){
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
    }*/


    }


    //application.properties 안에 선언해 놓은 친구를 데려옴
    @Value("${itemImgLocation}")
    private String itemImgLocation; //애플리케이션 프로퍼티에서 등록한 변수를 가져옴

    public void updateItemImg(MultipartFile itemImgFile, MegazineDTO megazineDTO) throws Exception {
        //이미지 파일이 들어왔으면
        if (!megazineDTO.getImageUrl().isEmpty()) {
            Megazine savedItemImg = megazineRepository.findById(megazineDTO.getMno())
                    .orElseThrow(EntityNotFoundException::new);

            //기존의 업로드 된 파일을 찾아서 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            //기존 이미지를 삭제되었으므로 새 파일을 업로드하고,
            // megazineDTO의 oriImgName, imgName, imgUrl도 update해야 함.
            //
            String oriImgName = megazineDTO.getOriImgName();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            megazineDTO.setImgName(imgName);
            megazineDTO.setOriImgName(oriImgName);
            megazineDTO.setImageUrl(imgUrl);

            Megazine megazine = megazineDTO.createItem();
            megazineRepository.save(megazine);

            //savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
      /*
      String oriImgName = itemImgFile.getOriginalFilename();
      String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
      String imgUrl = "/images/item" + imgName;
      savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
       */
        }
    }

    //readOnly = true : 상품 데이터를 읽기만 하므로 JPA에게 더티 체킹(변경 감지->업데이트)를 하지 않도록 설정.
    // 성능상 이점이 발생 (더 빨라짐)
    @Transactional(readOnly = true)
    public MegazineDTO getItemDtl(Long megazineId) {
        //매거진 mno 하나 불러와서 mapper불러서 megazineDtO 넣어주

    /*
    List<ItemImg> itemImgList = itemImgRepository.findByMegazine_MnoOrderByIdAsc(megazineId);
    List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
    for (ItemImg itemImg : itemImgList){
      ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);
      itemImgDTOList.add(itemImgDTO);
    }
*/
        //위에 이미지 읽었으니 아이템 읽어야즤
        Megazine megazine = megazineRepository.findById(megazineId)
                .orElseThrow(EntityNotFoundException::new);

        MegazineDTO megazineDTO = MegazineDTO.of(megazine); // 매핑해줌 아이템DTO로 변경해줌
        //앞에서 이미지 담아서 주려고
        /*megazineDTO.setItemImgDTOList(itemImgDTOList);*/

        return megazineDTO;
    }

    public void update(MegazineDTO megazineDTO, MultipartFile itemImgFile) throws Exception {
        // DTO를 엔티티로 변환
        Megazine megazine = megazineDTO.createItem();
        // 기존 게시글 조회
        Megazine existingBoard = megazineRepository.findById(megazine.getMno())
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + megazineDTO.getMno()));

        // 새로운 파일이 업로드된 경우 처리
        if (itemImgFile != null && !itemImgFile.isEmpty()) {
            updateItemImg(itemImgFile, megazineDTO);
        }

        // 제목과 내용 업데이트
        existingBoard.setTitle(megazine.getTitle());
        existingBoard.setContent(megazine.getContent());

        // 게시글 저장
        megazineRepository.save(existingBoard);
    }
  /*
  //이미지가 여러개일 때
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
  */

    //검색 기능
    public Page<Megazine> megazineSearchList(String searchKeyword, Pageable pageable){
        return megazineRepository.findByTitleContaining(searchKeyword, pageable);
    }


    public List<Megazine> getList() {
        return megazineRepository.findAll();
    }

    public Optional<Megazine> getMegazine(Long mno) {
        return megazineRepository.findById(mno);
    }

    public void save(Megazine megazine) {
        megazineRepository.save(megazine);
    }

    public void delete(Long mno) {
        megazineRepository.deleteById(mno);
    }


    // 게시글 조회 메서드 (megazineView)
    public MegazineDTO megazineView(Long mno) {
        Optional<Megazine> megazineOpt = megazineRepository.findById(mno);

        if (megazineOpt.isPresent()) {
            Megazine megazine = megazineOpt.get();
            megazine.setViewCount(megazine.getViewCount() + 1); // 조회수 증가
            megazineRepository.save(megazine); // 변경사항 저장
            return entityToDto(megazine); // DTO로 변환하여 반환
        }
        return null;
    }

    // 엔티티 -> DTO 변환 메서드
    public MegazineDTO entityToDto(Megazine megazine) {
        return new MegazineDTO(
                megazine.getMno(),
                megazine.getTitle(),
                megazine.getUser(),
                megazine.getContent(),
                megazine.getViewCount(),
                megazine.getCommentCount(),
                megazine.getScrapCount(),
                megazine.getOriImgName(),
                megazine.getImgName(),
                megazine.getImageUrl(),
                megazine.getRegTime(), // 등록 시간
                megazine.getUpdateTime(), // 수정 시간
                new ArrayList<>(), // ItemImgDTO 리스트 (초기화)
                new ArrayList<>()  // itemImgIds 리스트 (초기화)
        );

    }

    // DTO -> 엔티티 변환 메서드
    public Megazine dtoToEntity(MegazineDTO dto) {
        Megazine megazine = new Megazine();
        megazine.setMno(dto.getMno());
        megazine.setTitle(dto.getTitle());
        megazine.setUser(dto.getUser());
        megazine.setContent(dto.getContent());
        megazine.setViewCount(dto.getViewCount());
        megazine.setCommentCount(dto.getCommentCount());
        megazine.setScrapCount(dto.getScrapCount());
        megazine.setOriImgName(dto.getOriImgName());
        megazine.setImgName(dto.getImgName());
        megazine.setImageUrl(dto.getImageUrl());
        megazine.setRegTime(dto.getRegTime());
        megazine.setUpdateTime(dto.getUpdateTime());
        return megazine; // 여기에서 오류가 발생할 수 있음
    }



}