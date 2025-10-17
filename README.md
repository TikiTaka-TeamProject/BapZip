<div align="center">
    <h1>배달 주문 관리 플랫폼 Bap.zip!🍚</h1>
</div>

## 목차
- [1. 프로젝트 개요](#1-프로젝트-개요)
- [2. 프로젝트 관리](#2-프로젝트-관리)
- [3. 프로젝트 구상도](#3-프로젝트-구상도)
- [4. 기능 구현](#4-기능-구현)
- [5. 기술 문서](#5-기술-문서)

<br>

## 1. 프로젝트 개요
특정 지역(Ploygon 기반)과 카테고리 조건으로 가게를 검색하고 음식점의 배달 주문 관리, 주문 내역 관리, 사용자의 결제, 리뷰 작성 등의 기능을 제공하는 배달 플랫폼 개발

### ✨ 주요 기능
- JWT 기반 회원가입, 로그인 인증/인가 처리
- 음식점 검색 및 필터링(지역 기반) - 음식점 상세 조회 및 평균 평점 조회
- 사용자의 주문, 결제, 주문 건에 한하여 리뷰 등록 및 조회
- 관리자 승인 기반의 음식점 등록 프로세스
- 프롬프트 기반 AI 응답 활용

### 🛠️ 개발 환경
![Java](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white)

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)

![Toss Payments](https://img.shields.io/badge/Toss%20Payments-PG-0052CC?logo=toss&logoColor=white)
![Kakao Local API](https://img.shields.io/badge/Kakao%20Local%20API-FFCD00?logo=kakaotalk&logoColor=000000)
![Google AI](https://img.shields.io/badge/Google%20AI-genai--1.8.0-4285F4?logo=google&logoColor=white)

![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black)

![Git](https://img.shields.io/badge/Git-F05032?logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?logo=github&logoColor=white)

![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?logo=intellijidea&logoColor=white)


### 👥 팀 구성

감정보단 논리로 움직이는 6인의 T키타카 조입니다.

| name | role | mbti |                담당 파트                 |                                                                       Github                                                                        |
|:----:|:----:|:----:|:------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------:|
| 박용재  |  리더  | INTP | Ai API, Kakao Local 연동, Google Ai 연동 | <a href="https://github.com/SearchColor"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a> |
| 권용은  |  멤버  | ENTP |            가게, 리뷰 API                |   <a href="https://github.com/rlooko"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>    |
| 권재원  |  멤버  | ISTP |                                      |  <a href="https://github.com/ReadAlien"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>  |
| 안소나  |  멤버  | ISTP |                메뉴 API                |  <a href="https://github.com/sonaanweb"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>  |
| 오상경  |  멤버  | ISTJ |             결제, 카테고리 API             |   <a href="https://github.com/osk0521"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>   |
| 정인호  |  멤버  | ISTP |                유저 API                |    <a href="https://github.com/eNoLJ"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>    |


<br>


## 2. 프로젝트 관리

### 개발 기간
2025.09.26 - 2025.10.17

<details>
<summary><strong>협업 라이프사이클</strong></summary>

1. 브랜치 생성
2. 코드 작성
3. PR 생성
4. 리뷰 요청
5. dev 브랜치로 Merge
</details>

<details>
<summary><strong>이슈 관리</strong></summary>
<img width="1336" height="913" alt="issue" src="https://github.com/user-attachments/assets/625277bb-d1ae-4a6e-9c14-7efa9d10521a" />
</details>

<details>

<summary><strong>컨벤션</strong></summary>


- **Branch**
    - **전략**

      | Branch Type | Description |
      |-------------|-------------|
      | `dev`       | 주요 개발 branch, `main`으로 merge 전 거치는 branch |
      | `feature`   | 각자 개발할 branch, 기능 단위로 생성하기, 할 일 issue 등록 후 branch 생성 및 작업 |

    - **네이밍**
        - `{header}/#{issue number}`
        - 예) `feat/#1`

- **커밋 메시지 규칙**
    ```bash
    > type: 기능 요약 (: 뒤 한 칸 뛰고)

      - chore: 내부 파일 수정
      - feat: 새로운 기능 구현
      - add: feat 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성 시
      - fix: 코드 수정, 버그, 오류 해결
      - del: 쓸모없는 코드 삭제
      - docs: README나 WIKI 등의 문서 개정
      - move: 프로젝트 내 파일이나 코드의 이동
      - rename: 파일 이름의 변경
      - merge: 다른 브랜치를 merge하는 경우
      - style: 코드가 아닌 스타일 변경을 하는 경우
      - init: Initial commit을 하는 경우
      - refactor: 로직은 변경 없는 클린 코드를 위한 코드 수정
      
      ex) feat: 게시글 목록 조회 API 구현
    ```

- **Issue**
    ```bash
    📱 Description
    <!-- 진행할 작업을 설명해주세요 -->
    
    📱 To-do
    <!-- 작업을 수행하기 위해 해야할 태스크를 작성해주세요 -->
    [ ] todo1
    
    📱 ETC
    <!-- 특이사항 및 예정 개발 일정을 작성해주세요 -->
    ```

- **PR**
    - **규칙**
        - 기능 branch 작업 완료 후 PR 보내기
        - PR 후 슬랙에 공유하기
        - 최소 2명 이상의 동의를 받으면 merge
        - review 반영 후, 본인이 merge

    - **Template**
      ```bash
      📒 Issue Number
      <!-- 작업한 이슈 번호를 명시해주세요 -->
      
      📒 Description
      <!-- 작업 내용에 대한 설명을 적어주세요 -->
      
      📒 Test Result
      <!-- local에서 postman으로 요청한 결과를 첨부합니다 -->
      
      📒 To Reviewer
      <!-- 리뷰 받고 싶은 포인트를 작성합니다 -->
      
      ```
</details>

<br>

## 3. 프로젝트 구상도

### 🛠️ 아키텍처
<img width="892" height="567" alt="Image" src="https://github.com/user-attachments/assets/3e12f675-01fb-4058-82d6-dc766f88783d" />


### 🔗 ERD
<img width="3527" height="1612" alt="Image" src="https://github.com/user-attachments/assets/1ab5d484-9cdc-491c-9f90-5def5367b477" />





## 4. 기능 구현

### **✨ 유저**
- 회원가입
  - 회원 정보를 토대로 회원가입을 할 수 있음
  - MANAGER와 MASTER로 회원가입은 불가
  - 이메일 중복 해서 가입 불가
- 로그인
  - 이메일과 비밀번호를 이용한 로그인 기능
  - 로그인 성공 시 JWT 토큰 발급
  - 로그인 실패 시 401 Unauthorized 반환
- 전체 유저 조회
  - 회원 전체 목록을 조회할 수 있음
  - 페이지네이션 적용(page, size, sortBy, isAsc 지원)
  - MANAGER와 MASTER 권한만 접근 가능
- 단건 유저 조회
  - 유저 ID를 기반으로 단일 회원 정보 조회
  - 자기 자신의 정보는 누구나 확인 가능
  - 다른 회원 정보를 보려면 MANAGER와 MASTER 권한 필요
- 회원 정보 수정
  - 회원 본인만 수정 가능
  - 이름 및 비밀번호 변경 가능
  - 기존 비밀번호 확인 필요
- 회원 삭제
  - 회원 본인만 삭제 가능
  - 기존 비밀번호 확인 필요
  - soft delete 방식으로 처리
- 유저 권한 변경
  - MASTER 권한만 실행 가능
  - 다른 유저의 권한을 CUSTOMER, OWNER, MANAGER로 변경 가능
  - MASTER로는 변경 불가

### **✨ 메뉴**
- 가게의 `OWNER`로 등록된 사용자는 자신의 가게의 메뉴를 관리할 수 있습니다.
    - 메뉴 등록, 정보 수정, 상태(SOLD_OUT, AVAILABLE) 수정, 메뉴 삭제
- 사용자는 `메뉴 이름 검색`을 통해 원하는 메뉴를 판매하고 있는 가게 정보를 함께 조회할 수 있습니다.
- 관리자는 SOFT DELETE로 삭제 처리 된 메뉴까지 함께 조회가 가능합니다.


### **✨ 가게**
- 가게 정보 관리
    * 사용자가 가게를 등록할 수 있음
    * 가게 정보: 이름, 주소, 카테고리, 서비스 지역, 위치 정보(Point) 포함
* 가게 상세 조회
    * 특정 가게 ID 기준으로 상세 정보 조회 가능
    * 조회 가능한 정보
        * 가게 ID
        * 가게 이름
        * 주소
        * 현재 상태(PENDING, APPROVED, REJECTED)
        * 가게 소유자 이름
        * 카테고리 이름
        * 서비스 지역 이름
* 가게 정보 수정
    * Owner 권한이 있는 경우 수정 가능
    * 수정 가능한 정보
        * 이름
        * 주소
        * 카테고리
        * 위치(Point)
        * 서비스 지역은 좌표에 따라 자동으로 계산되며, Owner가 직접 수정 불가
* 승인(APPROVED) 된 가게 조회
    * Customer는 상태가 APPROVED인 가게만 조회 가능
* 상태별 가게 조회
    * 특정 상태에 따라 가게 목록을 조회할 수 있음
    * 상태를 지정하지 않으면 전체 가게 목록 조회 가능
    * 상태별 조회 가능한 값: PENDING, APPROVED, REJECTED
    * 반환 정보:
        * 가게 ID
        * 가게 이름
        * 주소
        * 현재 상태
        * 가게 소유자 이름
        * 카테고리 이름
        * 서비스 지역 이름
* 가게 삭제 (Soft Delete)
    * Owner 권한이 있는 경우 삭제 가능
    * 삭제 시 isDeleted 플래그가 true로 변경되며, 데이터는 실제로 DB에서 제거되지 않음
    * 삭제 시 deletedAt과 deletedBy가 기록됨
    * 이미 삭제된 가게를 삭제하려고 할 경우 예외 발생
    * 진행 중인 주문이 있는 가게는 삭제 불가

#### **✨ 주문**
* 결제
    * Toss Payments API를 활용한 결제 기능 구현
    * 결제 요청 시 주문 정보와 결제 정보를 함께 전송
    * 결제 성공 시 결제 상태를 'SUCCESS'로 업데이트
    * 결제 실패 시 결제 상태를 'FAILED'로 업데이트하고, 사용자에게 실패 사실 전달.
    * 결제 내역 저장
        * 결제 완료 후 결제 내역을 데이터베이스에 업데이트
        * 저장 정보: 결제 ID, 주문 ID, 결제 금액, 결제 상태, 결제 일시
* 결제 취소
    * 사용자가 결제 취소 요청 시, Toss Payments API를 통해 결제 취소 처리
    * 결제 취소 성공 시 결제 상태를 'CANCELLED'로 업데이트
    * 결제 취소 내역 저장
        * 결제 취소 후 취소 내역을 데이터베이스에 업데이트
        * 저장 정보: 결제 ID, 주문 ID, 취소 금액, 취소 일시

### **✨ 리뷰**
* 리뷰 작성
  * Customer 권한 사용자는 주문 완료 후 리뷰를 작성할 수 있음
  * 리뷰 정보: 점수(별점), 내용, 주문 ID, 가게 ID를 포함 
  * 리뷰 작성 시 다음과 같은 유효성 검증 수행 
    * 동일한 주문에 대해 중복 리뷰 작성 불가 
    * 존재하지 않는 주문 또는 가게 ID 전달 시 예외 발생 
  * 작성 완료 후 생성된 리뷰 ID, 작성 내용, 점수 등의 정보를 반환 
* 리뷰 조회 (가게 기준)
  * 특정 가게 ID 기준으로 해당 가게에 작성된 모든 리뷰를 조회 가능 
  * 반환 정보:
    * 리뷰 ID 
    * 작성자 이름 
    * 점수(별점)
    * 내용 
    * 작성일시
* 리뷰 조회 (내 리뷰)
  * 인증된 사용자는 자신이 작성한 모든 리뷰를 조회할 수 있음 
  * 특정 가게에 대해 자신이 작성한 리뷰만 별도로 조회하는 것도 가능 
  * 반환 정보:
    * 리뷰 ID 
    * 가게 이름 
    * 점수(별점)
    * 내용 
    * 작성일시
* 리뷰 수정 
  * Customer 권한 사용자는 자신이 작성한 리뷰만 수정 가능 
  * 부분 수정 가능 (예: 점수만 수정, 내용만 수정 등)
  * 존재하지 않는 리뷰 ID 또는 권한 없는 사용자가 요청 시 예외 발생 
  * 수정 후 최신 리뷰 정보 반환
* 리뷰 삭제 (Soft Delete)
  * Customer 권한 사용자는 자신이 작성한 리뷰만 삭제 가능 
  * 삭제 시 isDeleted 플래그가 true로 변경되고, deletedAt, deletedBy가 기록됨 
  * 실제 DB에서 데이터는 제거되지 않음 
  * 이미 삭제된 리뷰에 대해 재삭제 시 예외 발생

#### **✨ 카테고리**
* 카테고리 등록
    * MASTER 또는 MANAGER 권한이 있는 사용자는 새 카테고리를 등록할 수 있음
    * 등록 정보: 카테고리 이름(name), 설명(description), 등록일(createdAt), 등록자(createdBy)
    * 카테고리 이름과 설명은 필수 입력 항목이며 중복 불가

* 카테고리 목록 조회
    * 모든 사용자는 삭제되지 않은 카테고리 목록을 조회할 수 있음

* 전체 카테고리 조회(관리자용)
    * MASTER 또는 MANAGER 권한자는 삭제된 카테고리를 포함한 전체 목록 조회 가능

* 특정 카테고리 상세 조회

    * 카테고리 ID 기준으로 상세 정보 조회 가능

* 카테고리 수정
    * MASTER 또는 MANAGER 권한자가 수정 가능
    * 수정 가능한 정보: 이름(name), 설명(description)

* 카테고리 삭제 (Soft Delete)
    * MASTER 또는 MANAGER 권한자가 삭제 가능
    * 삭제 시 isDeleted 플래그가 true로 변경되고, 실제 데이터는 DB에서 제거되지 않음
    * 삭제 시 deletedAt, deletedBy 기록
    * 이미 삭제된 카테고리를 다시 삭제하려 할 경우 예외 발생

### **✨ 서비스 지역**
* 서비스 지역 생성
  * 서비스 지역 이름과 POLYGON geometry로 서비스 지역 data 생성
    * POLYGON geometry 로 특정 geometry가 속해있는지 판별하여 서비스 활성화 상태 결정

### **✨ AI**
* Ai-log 단건 조회
  * 특정 Ai-log Id 로 상세정보 조회
  * 조회 정보
    * ID
    * 유저 ID
    * 메뉴 ID
    * 질문한 내용
    * 답변 내용
    * 생성일
* Ai-log 전체 조회(로그인 유저)
  * 로그인한 유저의 Ai-log 전체 조회
  * 조회 정보는 단건 조회 정보의 list


<br>

## 5. 기술 문서

### 📄 Swagger
<details>
    <summary><strong>Swagger</strong></summary>
    <img width="1306" height="925" alt="BapZip swagger" src="https://github.com/user-attachments/assets/f4a9610a-b826-4a65-97e3-69a9e77f5e58" />
</details>

### 🔗 디렉토리 구조
<details>
    <summary><strong>디렉토리 구조</strong></summary>

```
    ├─main
│  ├─generated
│  ├─java
│  │  └─com
│  │      └─sparta
│  │          └─bapzip
│  │              │  BapzipApplication.java
│  │              │  
│  │              ├─ai
│  │              │  ├─application
│  │              │  │  │  AiCallable.java
│  │              │  │  │  AiServiceV1.java
│  │              │  │  │  
│  │              │  │  ├─dto
│  │              │  │  │      AiLogResponseDto.java
│  │              │  │  │      
│  │              │  │  └─exception
│  │              │  │          AiLogNotFoundException.java
│  │              │  │          
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  │      AiEntity.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          AiLogRepository.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │  ├─ai
│  │              │  │  │      GeminiApiClient.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          AiLogJpaRepository.java
│  │              │  │          AiLogRepositoryImpl.java
│  │              │  │          
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      │      AiControllerV1.java
│  │              │      │      
│  │              │      └─dto
│  │              │          ├─request
│  │              │          │      RequestDto.java
│  │              │          │      
│  │              │          └─response
│  │              │                  ResponseDto.java
│  │              │                  
│  │              ├─category
│  │              │  ├─application
│  │              │  │      CategoryServiceV1.java
│  │              │  │      
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  │      CategoryEntity.java
│  │              │  │  │      
│  │              │  │  ├─exception
│  │              │  │  │      CategoryException.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          CategoryRepository.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  │          CategoryJpaRepository.java
│  │              │  │          CategoryRepositoryImpl.java
│  │              │  │          
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      │      CategoryControllerV1.java
│  │              │      │      
│  │              │      └─dto
│  │              │          ├─request
│  │              │          │      CategoryRequestDto.java
│  │              │          │      
│  │              │          └─response
│  │              │                  CategoryDetailResponse.java
│  │              │                  
│  │              ├─global
│  │              │  ├─common
│  │              │  │      BaseEntity.java
│  │              │  │      
│  │              │  ├─exception
│  │              │  │      ErrorCode.java
│  │              │  │      ExceptionResponse.java
│  │              │  │      GlobalException.java
│  │              │  │      GlobalizedResponseException.java
│  │              │  │      
│  │              │  ├─infrastructure
│  │              │  │  └─config
│  │              │  │      ├─security
│  │              │  │      │      SecurityConfig.java
│  │              │  │      │      
│  │              │  │      └─swagger
│  │              │  │              SwaggerConfig.java
│  │              │  │              
│  │              │  └─response
│  │              │          ApiResponse.java
│  │              │          PageResponseDto.java
│  │              │          
│  │              ├─kakaolocal
│  │              │  ├─application
│  │              │  │  │  KakaoLocalCallable.java
│  │              │  │  │  KakaoLocalServiceV1.java
│  │              │  │  │  
│  │              │  │  ├─dto
│  │              │  │  │      KakaoLocalResponseDto.java
│  │              │  │  │      
│  │              │  │  └─exception
│  │              │  │          KakaoLocalResponseNotFoundException.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │      KakaoLocalApiClient.java
│  │              │  │      
│  │              │  └─presentation
│  │              │          KakaoLocalApiController.java
│  │              │          
│  │              ├─menu
│  │              │  ├─application
│  │              │  │  │  MenuServiceV1.java
│  │              │  │  │  
│  │              │  │  ├─dto
│  │              │  │  │  └─request
│  │              │  │  │          MenuCreateRequest.java
│  │              │  │  │          MenuSearchRequest.java
│  │              │  │  │          MenuStatusUpdateRequest.java
│  │              │  │  │          MenuUpdateRequest.java
│  │              │  │  │          
│  │              │  │  └─exception
│  │              │  │          InvalidMenuIdException.java
│  │              │  │          MenuNotFoundException.java
│  │              │  │          
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  │      MenuEntity.java
│  │              │  │  │      
│  │              │  │  ├─enums
│  │              │  │  │      MenuStatus.java
│  │              │  │  │      
│  │              │  │  ├─exception
│  │              │  │  │      InvalidMenuStatusException.java
│  │              │  │  │      MenuAlreadyDeletedException.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          MenuRepository.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  │          MenuJpaRepository.java
│  │              │  │          MenuRepositoryImpl.java
│  │              │  │          
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      │      MenuControllerV1.java
│  │              │      │      
│  │              │      └─dto
│  │              │          └─response
│  │              │                  MenuAdminResponse.java
│  │              │                  MenuCreateResponse.java
│  │              │                  MenuDetailResponse.java
│  │              │                  MenuListByShopResponse.java
│  │              │                  MenuSearchResponse.java
│  │              │                  
│  │              ├─order
│  │              │  ├─application
│  │              │  │  │  OrderServiceV1.java
│  │              │  │  │  
│  │              │  │  ├─dto
│  │              │  │  │  │  OrderCreationDto.java
│  │              │  │  │  │  OrderDetailDto.java
│  │              │  │  │  │  OrderDto.java
│  │              │  │  │  │  OrderMenuInfo.java
│  │              │  │  │  │  ShopOrderDto.java
│  │              │  │  │  │  
│  │              │  │  │  └─request
│  │              │  │  │          CreateOrderRequest.java
│  │              │  │  │          
│  │              │  │  └─exception
│  │              │  │          MenusNotFoundInOrderException.java
│  │              │  │          OrderNotFoundException.java
│  │              │  │          
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  │      OrderEntity.java
│  │              │  │  │      
│  │              │  │  ├─enums
│  │              │  │  │      OrderStatus.java
│  │              │  │  │      
│  │              │  │  ├─exception
│  │              │  │  │      ForbiddenOrderAccessException.java
│  │              │  │  │      MenuNotInShopException.java
│  │              │  │  │      OrderNotAcceptedException.java
│  │              │  │  │      OrderNotCancellableException.java
│  │              │  │  │      OrderNotCompletedException.java
│  │              │  │  │      OrderNotCookCompletedException.java
│  │              │  │  │      OrderNotCookingException.java
│  │              │  │  │      OrderNotDeliveredException.java
│  │              │  │  │      OrderNotDeliveringException.java
│  │              │  │  │      OrderNotPendingException.java
│  │              │  │  │      SoldOutMenuException.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          OrderRepository.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  │          OrderJpaRepository.java
│  │              │  │          OrderRepositoryImpl.java
│  │              │  │          
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      │      OrderControllerV1.java
│  │              │      │      
│  │              │      └─dto
│  │              │          └─response
│  │              │                  CreateOrderResponse.java
│  │              │                  OrderDetailResponse.java
│  │              │                  OrderResponse.java
│  │              │                  ShopOrderResponse.java
│  │              │                  
│  │              ├─ordermenu
│  │              │  ├─application
│  │              │  │      OrderMenuServiceV1.java
│  │              │  │      
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  │      OrderMenuEntity.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          OrderMenuRepository.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  │          OrderMenuJpaRepository.java
│  │              │  │          OrderMenuRepositoryImpl.java
│  │              │  │          
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      │      OrderMenuControllerV1.java
│  │              │      │      
│  │              │      └─dto
│  │              │          ├─request
│  │              │          │      RequestDto.java
│  │              │          │      
│  │              │          └─response
│  │              │                  ResponseDto.java
│  │              │                  
│  │              ├─payment
│  │              │  ├─application
│  │              │  │      PaymentServiceV1.java
│  │              │  │      
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  │      PaymentEntity.java
│  │              │  │  │      PaymentStatusEnum.java
│  │              │  │  │      
│  │              │  │  ├─exception
│  │              │  │  │      PaymentException.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          PaymentRepository.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │  ├─config
│  │              │  │  │  └─payment
│  │              │  │  │          TossPaymentsConfig.java
│  │              │  │  │          
│  │              │  │  └─repository
│  │              │  │          PaymentJpaRepository.java
│  │              │  │          PaymentRepositoryImpl.java
│  │              │  │          
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      │      PaymentControllerV1.java
│  │              │      │      
│  │              │      └─dto
│  │              │          ├─request
│  │              │          │      PaymentCancelRequest.java
│  │              │          │      PaymentCreateRequest.java
│  │              │          │      
│  │              │          └─response
│  │              │                  PaymentResponseDto.java
│  │              │                  
│  │              ├─review
│  │              │  ├─application
│  │              │  │  │  ReviewServiceV1.java
│  │              │  │  │  
│  │              │  │  ├─dto
│  │              │  │  │  │  ReviewDto.java
│  │              │  │  │  │  
│  │              │  │  │  └─request
│  │              │  │  │          CreateReviewRequest.java
│  │              │  │  │          UpdateReviewRequest.java
│  │              │  │  │          
│  │              │  │  └─exception
│  │              │  │          DuplicateReviewException.java
│  │              │  │          ReviewNotFoundException.java
│  │              │  │          UnauthorizedReviewAccessException.java
│  │              │  │          
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  │      ReviewEntity.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          ReviewRepository.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  │          ReviewJpaRepository.java
│  │              │  │          ReviewRepositoryImpl.java
│  │              │  │          
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      │      ReviewControllerV1.java
│  │              │      │      
│  │              │      └─dto
│  │              │          └─response
│  │              │                  ReviewCreateResponse.java
│  │              │                  
│  │              ├─servicearea
│  │              │  ├─application
│  │              │  │  │  ServiceAreaServiceV1.java
│  │              │  │  │  
│  │              │  │  ├─dto
│  │              │  │  │  │  AreaReturnDto.java
│  │              │  │  │  │  AreaSaveDto.java
│  │              │  │  │  │  
│  │              │  │  │  └─request
│  │              │  │  │          AreaSaveRequest.java
│  │              │  │  │          
│  │              │  │  └─exception
│  │              │  │          ServiceAreaNotFoundException.java
│  │              │  │          
│  │              │  ├─domain
│  │              │  │  │  Point.java
│  │              │  │  │  
│  │              │  │  ├─entity
│  │              │  │  │      ServiceAreaEntity.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          ServiceAreaRepository.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  │          ServiceAreaJpaRepository.java
│  │              │  │          ServiceAreaRepositoryImpl.java
│  │              │  │          
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      │      ServiceAreaControllerV1.java
│  │              │      │      
│  │              │      └─dto
│  │              │          └─response
│  │              │                  AreaSaveResponse.java
│  │              │                  ResponseDto.java
│  │              │                  
│  │              ├─shop
│  │              │  ├─application
│  │              │  │  │  ShopServiceV1.java
│  │              │  │  │  
│  │              │  │  ├─dto
│  │              │  │  │  └─request
│  │              │  │  │          ShopCreationRequest.java
│  │              │  │  │          ShopUpdateRequest.java
│  │              │  │  │          
│  │              │  │  └─exception
│  │              │  │          CoordinateOutOfRangeException.java
│  │              │  │          OwnerNotFoundException.java
│  │              │  │          ShopAlreadyExistsException.java
│  │              │  │          ShopDeleteForbidden.java
│  │              │  │          ShopNotFoundException.java
│  │              │  │          UnauthorizedShopAccessException.java
│  │              │  │          
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  │      ShopEntity.java
│  │              │  │  │      
│  │              │  │  ├─enums
│  │              │  │  │      ShopStatusEnum.java
│  │              │  │  │      
│  │              │  │  ├─exception
│  │              │  │  │      ShopAlreadyDeletedException.java
│  │              │  │  │      
│  │              │  │  └─repository
│  │              │  │          ShopRepository.java
│  │              │  │          
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  │          ShopJpaRepository.java
│  │              │  │          ShopRepositoryImpl.java
│  │              │  │          
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      │      ShopControllerV1.java
│  │              │      │      
│  │              │      └─dto
│  │              │          └─response
│  │              │                  CreateShopResponse.java
│  │              │                  ShopDetailForUserResponse.java
│  │              │                  ShopDetailResponse.java
│  │              │                  
│  │              └─user
│  │                  ├─application
│  │                  │  │  UserDetailsServiceImpl.java
│  │                  │  │  UserServiceV1.java
│  │                  │  │  
│  │                  │  ├─dto
│  │                  │  │  └─request
│  │                  │  │          LoginRequestDto.java
│  │                  │  │          SignupRequestDto.java
│  │                  │  │          UserDeleteRequestDto.java
│  │                  │  │          UserRoleChangeRequestDto.java
│  │                  │  │          UserUpdateRequestDto.java
│  │                  │  │          
│  │                  │  └─excpetion
│  │                  │          DuplicateUserException.java
│  │                  │          PasswordNotMatchException.java
│  │                  │          UnauthorizedUserException.java
│  │                  │          UserNotFoundException.java
│  │                  │          
│  │                  ├─domain
│  │                  │  ├─entity
│  │                  │  │      UserDetailsImpl.java
│  │                  │  │      UserEntity.java
│  │                  │  │      
│  │                  │  ├─enums
│  │                  │  │      UserRoleEnum.java
│  │                  │  │      
│  │                  │  └─repository
│  │                  │          UserRepository.java
│  │                  │          
│  │                  ├─infrastructure
│  │                  │  └─repository
│  │                  │          UserJpaRepository.java
│  │                  │          UserRepositoryImpl.java
│  │                  │          
│  │                  ├─jwt
│  │                  │      JwtAuthenticationFilter.java
│  │                  │      JwtAuthorizationFilter.java
│  │                  │      JwtUtil.java
│  │                  │      
│  │                  └─presentation
│  │                      ├─controller
│  │                      │      UserControllerV1.java
│  │                      │      
│  │                      └─dto
│  │                          └─response
│  │                                  SignupResponseDto.java
│  │                                  UserDeleteResponseDto.java
│  │                                  UserResponseDto.java
│  │                                  UserRoleChangeResponseDto.java
│  │                                  UserUpdateResponseDto.java
│  │                                  
│  └─resources
│          application-dev.yml
│          application-prod.yml
│          application.yml
│          secrets.yml
│          
└─test
    ├─java
    │  └─com
    │      └─sparta
    │          └─bapzip
    │              │  BapzipApplicationTests.java
    │              │  
    │              ├─category
    │              │  ├─application
    │              │  │      CategoryServiceV1Test.java
    │              │  │      
    │              │  └─domain
    │              │      └─entity
    │              │              CategoryEntityTest.java
    │              │              
    │              ├─kakaolocal
    │              │  └─application
    │              │          KakaoLocalServiceV1Test.java
    │              │          
    │              ├─menu
    │              │  ├─application
    │              │  │      MenuServiceV1Test.java
    │              │  │      
    │              │  └─domain
    │              │      └─entity
    │              │              MenuEntityTest.java
    │              │              
    │              ├─order
    │              │  ├─application
    │              │  │      OrderServiceV1Test.java
    │              │  │      
    │              │  └─domain
    │              │      └─entity
    │              │              OrderEntityTest.java
    │              │              
    │              ├─review
    │              │  ├─application
    │              │  │      ReviewServiceV1Test.java
    │              │  │      
    │              │  └─domain
    │              │      └─entity
    │              │              ReviewEntityTest.java
    │              │              
    │              ├─servicearea
    │              │  └─application
    │              │          ServiceAreaServiceV1Test.java
    │              │          
    │              └─user
    │                  ├─application
    │                  │      UserServiceV1Test.java
    │                  │      
    │                  ├─domain
    │                  │  └─entity
    │                  │          UserEntityTest.java
    │                  │          
    │                  └─presentation
    │                      └─controller
    │                              UserControllerV1Test.java
    │                              
    └─resources
            application-test.yml

```
</details>

### 📄 API 명세서
[API 명세서 자세히 보기](https://teamsparta.notion.site/27a2dc3ef51481e1b791ca087368d2af?v=27a2dc3ef51481498f04000c69f7fb79)

