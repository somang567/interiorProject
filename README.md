# 2030세대를 위한 셀프인테리어 웹사이트
---
스프링 부트 : thymeleaf + ajax + api 프로젝트

## ✏️프로젝트 소개
|서비스 명|아트 인 홈|
|------|------|
|서비스 로고|![logo 4](https://github.com/user-attachments/assets/c9092b44-2a21-4bfc-99a0-391adb6085ce)|
|서비스 설명 |셀프 인테리어를 희망하는 2030세대를 위한 웹사이트입니다.|

## ⏳개발기간
2024.09.01~2024.10.28 (약 2개월)

## 🤼‍♀️멤버구성 및 역할
- 팀장 박민영
  로그인, 회원가입, 꿀팁 매거진 CRUD, 검색기능, ajax 매거진 스크랩 기능, ajax 댓글기능, 마이페이지 매거진 스크랩 연결, 페이지네이션, 일정관리 및 회의록 작성.
- 팀원 김태준
  설계(DB테이블 , 유스케이스), 자제 상품 페이지 CRUD, 다중 이미지 업로드(대표 이미지), 상품 문의 게시판, 검색(상품 명, 상품상세 내용, 작성자), 댓글 CRUD, 페이지네이션.
- 팀원 정종민
  게시판 CRUD(묻고 답하기, 업체 홍보, 셀프 인테리어), ajax 댓글 기능 구현, 댓글 CRUD , 검색(제목, 내용, 제목+내용), 페이지네이션. 
- 팀원 임진아
  메인페이지 구현, 마이페이지 게시판 연결, 정보수정 구현, UI 구성(헤더, 푸터 레이아웃).
- 팀원 이기련
  인테리어 업체 검색 기능,페이지네이션, 공공데이터 API, 구글맵 API 사용.

## ✏️개발 환경
<table>
    <tr>
      <th>속성</th>
      <th>버전/도구</th>
    </tr>
    <tr>
      <td>Java</td>
      <td>OpenJDK 11</td>
    </tr>
    <tr>
      <td>Gradle</td>
      <td>2.7.18</td>
    </tr>
    <tr>
      <td>IDE</td>
      <td>IntelliJ IDEA 2024.2.3</td>
    </tr>
    <tr>
      <td>FrameWork</td>
      <td>springBoot 2.7.18</td>
    </tr>
    <tr>
      <td>DB</td>
      <td>MariaDB (Tools : HeidiSQL)</td>
    </tr>
</table>

<h1>🖊️ 디자인</h1>
<a style="display: flex;" href="https://www.figma.com/?gad_source=1&gclid=Cj0KCQjwsoe5BhDiARIsAOXVoUsJka6YCtrhkvqra87DUVNSvr2kxpOmVRkQsI5u3MVyym41FojenhcaAujSEALw_wcB">Figma </a> 

<div style="display: flex;">
  <h2>Main Stack</h2>
  <img src="https://img.shields.io/badge/intellijidea-F57C00?style=flat&logo=intellijidea&logoColor=#white"/>
  <img src="https://img.shields.io/badge/gradle-02303A?style=flat&logo=gradle&logoColor=#white"/>
  <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=OpenJDK&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=Spring&logoColor=#white"/>
  <img src="https://img.shields.io/badge/html5-E34F26style=flat&logo=html5&logoColor=#white"/>
  <img src="https://img.shields.io/badge/css3-1572B6?style=flat&logo=css3&logoColor=#white"/>
  <img src="https://img.shields.io/badge/javaScript-F7DF1E?style=flat&logo=javaScript&logoColor=#black"/>
  <img src="https://img.shields.io/badge/github-181717?style=flat&logo=github&logoColor=#white"/>

  
<div style="display:flex;">  
  <h2>library</h2>
  <img src="https://img.shields.io/badge/mariadb-003545?style=flat&logo=mariadb&logoColor=#white"/>
  <img src="https://img.shields.io/badge/thymeleaf-005F0F?style=flat&logo=Thymeleaf&logoColor=#white"/>
  <img src="https://img.shields.io/badge/git-F05032?style=flat&logo=git&logoColor=#white"/>


  
  

<h1>주요 기능</h1>
<ul> 로그인
  <li>사용자는 회원가입된 사용자/관리자 게정으로 로그인이 가능합니다.</li>
</ul>
<ul> 회원가입
  <li>비회원인 사용자는 홈페이지의 이용목적에 따라 사용자와 관리자(업체)로 회원가입 할 수 있습니다.</li>
</ul>
<ul> 회원정보 수정
  <li>로그인된 사용자는 자신의 정보를 수정할 수 있습니다.</li>
</ul>
<ul> 상품찾기
  <li>유저는 등록된 각 인테리어자재소품들을 찾아볼 수 있습니다.</li>
  <li>관리자는 상품을 등록 수정 삭제를 할 수 있습니다.</li>
  <li>유저와 사용자 모두 자신의 게시물이 아니면 조회만 가능합니다.</li>
</ul>
<ul> *Ajax 댓글 기능*
  <li>사용자는 댓글을 작성할 수 있습니다.</li>
  <li>상품 문의게시판의 경우 관리자만 댓글등록이 가능합니다.</li>
</ul>
<ul> *Ajax 스크랩 및 찜목록 기능*
  <li>회원은 매거진을 스크랩하여 마이페이지에서 조회가 가능합니다.</li>
  <li>회원은 상품을 찜하여 마이페이지에서 조회가 가능합니다.</li>
</ul>
<ul> *검색*
  <li>상품찾기에서의 검색은 상품명,상품 상세내용,작성자로 구분하여 검색 가능합니다.</li>
  <li>매거진 , 게시판탭에서는 제목 , 제목+내용등 키워드로 검색이 가능합니다.</li>
</ul>
<ul> 공공데이터 API(업체찾기, 구글 맵)
  <li>사용자는 각 도시별 시/군/구로 각 상품업체를 조회할 수 있습니다.</li>
</ul>


## 📺화면 구성
|메인페이지|상품보기|팁 앤 매거진|
|------|---|---|
|![main](https://github.com/user-attachments/assets/6935f248-f2f3-4e04-be0a-c36b83dbe411)|![product](https://github.com/user-attachments/assets/98397960-d54f-4852-9d23-5099b8eecd83)|![tipNmegazine](https://github.com/user-attachments/assets/a684f125-3cf7-415c-8444-146eb942e668)|


|업체조회|게시판 (업체홍보, 셀프 인테리어, 묻고 답하기)|마이페이지|
|---|---|---|
|![searchFirm2](https://github.com/user-attachments/assets/8f0440e4-bda5-4ba3-9967-149103fe18f1)|![boardGroup](https://github.com/user-attachments/assets/deca4c6d-5c81-4e90-a306-1af965135c5c)|![myPage](https://github.com/user-attachments/assets/ed2b571d-7f40-4a22-8631-33e5d33174ea)|


## 📽️시현영상 및 PPT 링크 참조
https://drive.google.com/drive/folders/1fNqljigny3Kaieq7AMtRs83iW48MsGyU?usp=drive_link

## 프로젝트 구조
```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂keduit
 ┃ ┃ ┃ ┃ ┗ 📂interiors
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂constant
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┣ 📂resources
 ┃ ┃ ┣ 📂static
 ┃ ┃ ┃ ┣ 📂css
 ┃ ┃ ┃ ┗ 📂img
 ┃ ┃ ┣ 📂templates
 ┃ ┃ ┃ ┣ 📂boards
 ┃ ┃ ┃ ┣ 📂cs
 ┃ ┃ ┃ ┣ 📂fragments
 ┃ ┃ ┃ ┣ 📂layout
 ┃ ┃ ┃ ┣ 📂megazine
 ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┣ 📂product
 ┃ ┃ ┃ ┣ 📂search
 ┣ 📂test
 ┃ ┗ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂keduit
 ┃ ┃ ┃ ┃ ┗ 📂interiors
 ┃ ┃ ┃ ┃ ┃ ┗ 📜InteriorsApplicationTests.java
 ┗ 📜.DS_Store
```


