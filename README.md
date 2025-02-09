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
<ul>개인 블로그 크롤링 Data Read
  <li>크롤링한 데이터를 SpringBoot 엔티티를 하나 만들어 MySQL Database에 저장하였습니다.</li>
</ul>
<ul>JSOUP 크롤링 라이브러리 사용 
  <li>정적인 데이터를 사용자에게 보다 빠르게 보여지기 위해 JSOUP 라이브러리를 사용하여 크롤링을 진행하였습니다. 135개의 블로그 중 총 20개의 블로그를 가져올 수 있도록 구성하였습니다.</li>
</ul>
<ul> 페이지네이션
  <li>데이터베이스에 저장된 크롤링 데이터를 Ajax GET 요청으로 읽어와서 태그를 동적으로 생성하여 화면에 보이도록 하였고 페이지네이션 함수를 작성하여 함께 화면에 보이도록 구성하였습니다.
    <br>한 한 화면에 4개의 블로그가 보이도록 구성하였고 총 5페이지의 페이지 번호가 보이도록 하였습니다. 왼쪽 및 오른쪽 화살표로 화면 이동이 가능합니다.</li>
</ul>


## 프로젝트 구조
```
D:.
│  .gitattributes
│  .gitignore
│  build.gradle
│  gradlew
│  gradlew.bat
│  HELP.md
│  README.md
│  settings.gradle
│  
├─.gradle
│  │  file-system.probe
│  │  
│  ├─8.11.1
│  │  │  gc.properties
│  │  │  
│  │  ├─checksums
│  │  │      checksums.lock
│  │  │      md5-checksums.bin
│  │  │      sha1-checksums.bin
│  │  │      
│  │  ├─executionHistory
│  │  │      executionHistory.bin
│  │  │      executionHistory.lock
│  │  │      
│  │  ├─expanded
│  │  ├─fileChanges
│  │  │      last-build.bin
│  │  │      
│  │  ├─fileHashes
│  │  │      fileHashes.bin
│  │  │      fileHashes.lock
│  │  │      resourceHashesCache.bin
│  │  │      
│  │  └─vcsMetadata
│  ├─buildOutputCleanup
│  │      buildOutputCleanup.lock
│  │      cache.properties
│  │      outputFiles.bin
│  │      
│  └─vcs-1
│          gc.properties
│          
├─.idea
│  │  .gitignore
│  │  compiler.xml
│  │  dbnavigator.xml
│  │  gradle.xml
│  │  jarRepositories.xml
│  │  misc.xml
│  │  modules.xml
│  │  uiDesigner.xml
│  │  vcs.xml
│  │  workspace.xml
│  │  
│  └─modules
│          Resume.main.iml
│          
├─build
│  │  resolvedMainClassName
│  │  
│  ├─classes
│  │  └─java
│  │      ├─main
│  │      │  └─com
│  │      │      └─eco
│  │      │          └─Resume
│  │      │              │  ResumeApplication.class
│  │      │              │  
│  │      │              ├─constant
│  │      │              │      Role.class
│  │      │              │      
│  │      │              ├─controller
│  │      │              │      BlogApiController.class
│  │      │              │      MainController.class
│  │      │              │      TestController.class
│  │      │              │      
│  │      │              ├─dto
│  │      │              │      BlogsDTO$BlogsDTOBuilder.class
│  │      │              │      BlogsDTO.class
│  │      │              │      BlogsScrapDTO.class
│  │      │              │      ImageUrlRequest.class
│  │      │              │      
│  │      │              ├─entity
│  │      │              │      BaseEntity.class
│  │      │              │      BaseTimeEntity.class
│  │      │              │      Blogs.class
│  │      │              │      
│  │      │              ├─initializer
│  │      │              │      DataInitializer.class
│  │      │              │      
│  │      │              ├─repository
│  │      │              │      BlogsRepository.class
│  │      │              │      
│  │      │              ├─restTemplate
│  │      │              │      AppConfig.class
│  │      │              │      
│  │      │              └─service
│  │      │                      BlogsService.class
│  │      │                      ExternalService.class
│  │      │                      
│  │      └─test
│  │          └─com
│  │              └─eco
│  │                  └─Resume
│  │                          CrawlingServiceTest.class
│  │                          ResumeApplicationTests.class
│  │                          
│  ├─generated
│  │  └─sources
│  │      ├─annotationProcessor
│  │      │  └─java
│  │      │      ├─main
│  │      │      └─test
│  │      └─headers
│  │          └─java
│  │              ├─main
│  │              └─test
│  ├─libs
│  │      Resume-0.0.1-SNAPSHOT-plain.jar
│  │      Resume-0.0.1-SNAPSHOT.jar
│  │      
│  ├─reports
│  │  └─tests
│  │      └─test
│  │          │  index.html
│  │          │  
│  │          ├─classes
│  │          │      com.eco.Resume.CrawlingServiceTest.html
│  │          │      com.eco.Resume.ResumeApplicationTests.html
│  │          │      
│  │          ├─css
│  │          │      base-style.css
│  │          │      style.css
│  │          │      
│  │          ├─js
│  │          │      report.js
│  │          │      
│  │          └─packages
│  │                  com.eco.Resume.html
│  │                  
│  ├─resources
│  │  └─main
│  │      │  application.properties
│  │      │  
│  │      ├─driver
│  │      ├─static
│  │      │  ├─css
│  │      │  │      common.css
│  │      │  │      footer.css
│  │      │  │      header.css
│  │      │  │      information.css
│  │      │  │      main.css
│  │      │  │      skill.css
│  │      │  │      
│  │      │  ├─img
│  │      │  │      artInHome.png
│  │      │  │      back.png
│  │      │  │      default-img.png
│  │      │  │      ecoLogo.png
│  │      │  │      faceImg.png
│  │      │  │      next.png
│  │      │  │      plasticWorld.png
│  │      │  │      publicApi.png
│  │      │  │      tourUs.png
│  │      │  │      
│  │      │  └─javascript
│  │      │          blogPagination.js
│  │      │          information.js
│  │      │          skill.js
│  │      │          
│  │      └─templates
│  │          │  main.html
│  │          │  
│  │          ├─fragments
│  │          │      footer.html
│  │          │      header.html
│  │          │      
│  │          ├─layout
│  │          │      layout.html
│  │          │      
│  │          └─member
│  │                  login.html
│  │                  memberForm.html
│  │                  memberRegisterForm.html
│  │                  myPage.html
│  │                  
│  ├─test-results
│  │  └─test
│  │      │  TEST-com.eco.Resume.CrawlingServiceTest.xml
│  │      │  TEST-com.eco.Resume.ResumeApplicationTests.xml
│  │      │  
│  │      └─binary
│  │              output.bin
│  │              output.bin.idx
│  │              results.bin
│  │              
│  └─tmp
│      ├─bootJar
│      │      MANIFEST.MF
│      │      
│      ├─compileJava
│      │      previous-compilation-data.bin
│      │      
│      ├─compileTestJava
│      │      previous-compilation-data.bin
│      │      
│      ├─jar
│      │      MANIFEST.MF
│      │      
│      └─test
├─gradle
│  └─wrapper
│          gradle-wrapper.jar
│          gradle-wrapper.properties
│          
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─eco
    │  │          └─Resume
    │  │              │  ResumeApplication.java
    │  │              │  
    │  │              ├─config
    │  │              │      CustomAuthenticationEntryPoint.java
    │  │              │      SecurityConfig.java
    │  │              │      
    │  │              ├─constant
    │  │              │      Role.java
    │  │              │      
    │  │              ├─controller
    │  │              │      BlogApiController.java
    │  │              │      MainController.java
    │  │              │      MemberController.java
    │  │              │      TestController.java
    │  │              │      
    │  │              ├─driver
    │  │              │      WebDriverConfig.java
    │  │              │      
    │  │              ├─dto
    │  │              │      BlogsDTO.java
    │  │              │      BlogsScrapDTO.java
    │  │              │      ImageUrlRequest.java
    │  │              │      MemberDTO.java
    │  │              │      
    │  │              ├─entity
    │  │              │      BaseEntity.java
    │  │              │      BaseTimeEntity.java
    │  │              │      Blogs.java
    │  │              │      BlogsScrap.java
    │  │              │      Member.java
    │  │              │      
    │  │              ├─initializer
    │  │              │      DataInitializer.java
    │  │              │      
    │  │              ├─repository
    │  │              │      BlogsRepository.java
    │  │              │      BlogsScrapRepository.java
    │  │              │      MemberRepository.java
    │  │              │      
    │  │              ├─restTemplate
    │  │              │      AppConfig.java
    │  │              │      
    │  │              └─service
    │  │                      BlogCrawlingService.java
    │  │                      BlogScrapService.java
    │  │                      BlogsService.java
    │  │                      CrawlingExample.java
    │  │                      ExternalService.java
    │  │                      MemberService.java
    │  │                      
    │  └─resources
    │      │  application.properties
    │      │  
    │      ├─driver
    │      ├─static
    │      │  ├─css
    │      │  │      common.css
    │      │  │      footer.css
    │      │  │      header.css
    │      │  │      information.css
    │      │  │      main.css
    │      │  │      skill.css
    │      │  │      
    │      │  ├─img
    │      │  │      artInHome.png
    │      │  │      back.png
    │      │  │      default-img.png
    │      │  │      ecoLogo.png
    │      │  │      faceImg.png
    │      │  │      next.png
    │      │  │      plasticWorld.png
    │      │  │      publicApi.png
    │      │  │      tourUs.png
    │      │  │      
    │      │  └─javascript
    │      │          blogPagination.js
    │      │          information.js
    │      │          skill.js
    │      │          
    │      └─templates
    │          │  main.html
    │          │  
    │          ├─fragments
    │          │      footer.html
    │          │      header.html
    │          │      
    │          ├─layout
    │          │      layout.html
    │          │      
    │          └─member
    │                  login.html
    │                  memberForm.html
    │                  memberRegisterForm.html
    │                  myPage.html
    │                  
    └─test
        └─java
            └─com
                └─eco
                    └─Resume
                            CrawlingServiceTest.java
                            ResumeApplicationTests.java
                            

```


