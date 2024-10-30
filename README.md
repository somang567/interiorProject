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

## 🤼‍♀️멤버구성
- 팀장 박민영: 로그인, 회원가입, 꿀팁 매거진 CRUD, 검색기능, ajax 매거진 스크랩 기능, ajax 댓글기능, 마이페이지 매거진 스크랩 연결, 페이지네이션, 일정관리 및 회의록 작성.
- 팀원 김태준: 설계(DB테이블 , 유스케이스), 자제 상품 페이지 CRUD, 다중 이미지 업로드(thumbnail 구현), 상품 문의 게시판, 검색(상품 명, 상품상세 내용, 작성자), 댓글 CRUD, 페이지네이션.
- 팀원 정종민: 게시판 CRUD(묻고 답하기, 업체 홍보, 셀프 인테리어), ajax 댓글 기능 구현, 댓글 CRUD , 검색(제목, 내용, 제목+내용), 페이지네이션. 
- 팀원 임진아: 메인페이지 구현, 마이페이지 게시판 연결, 정보수정 구현, UI 구성(헤더, 푸터 레이아웃).
- 팀원 이기련: 인테리어 업체 검색 기능,페이지네이션, 공공데이터 API, 구글맵 API 사용.

## ✏️개발 환경
- java 11
- JDK: 2.7.18
- IDEA: 2024.2.3
- **Framework** : springBoot(2.7.18)
- **Database**: Maria DB (HeidiSQL)

## ✏️주요 기능
#### 로그인
#### 회원가입
#### 회원정보 수정
#### 게시판 CRUD
#### 매거진 CURD
#### ajax 댓글 기능
#### ajax 스크랩 및 찜목록 기능
#### 검색 기능
#### 공공데이터 API(업체찾기, 구글 맵) 

## 📺화면 구성
|메인페이지|상품보기|팁 앤 매거진|
|------|---|---|
|<img src="https://github.com/user-attachments/assets/b84137da-8338-40b8-b852-6ec6e3a68d4e.png" width="200" height="400"/>|테스트2|테스트3|

|업체조회|게시판|마이페이지|
|---|---|---|
|테스트1|테스트2|테스트3|

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


