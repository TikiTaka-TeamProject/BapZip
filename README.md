<div align="center">
    <h1>배달 주문 관리 플랫폼 Bap.zip!🍚</h1>
</div>

## 목차
- [1. 프로젝트 개요](#1-프로젝트-개요)
- [2. 프로젝트 관리](#2-프로젝트-관리)
- [3. 프로젝트 구상도](#3-프로젝트-구상도)
- [4. 구현 기능](#4-구현-기능)
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

| name | role | mbti | 담당 파트 |                                                                       Github                                                                        |
|:----:|:----:|:----:|:-----:|:---------------------------------------------------------------------------------------------------------------------------------------------------:|
| 박용재  |  리더  | INTP |Ai API, Kakao Local 연동, Google Ai 연동  | <a href="https://github.com/SearchColor"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a> |
| 권용은  |  멤버  | ENTP |가게, 리뷰 API |   <a href="https://github.com/rlooko"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>    |
| 권재원  |  멤버  | ISTP |주문API, CI/CD |  <a href="https://github.com/ReadAlien"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>  |
| 안소나  |  멤버  | ISTP |메뉴 API |  <a href="https://github.com/sonaanweb"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>  |
| 오상경  |  멤버  | ISTJ |결제, 카테고리 API |   <a href="https://github.com/osk0521"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>   |
| 정인호  |  멤버  | ISTP |유저 API    |    <a href="https://github.com/eNoLJ"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>    |

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

      | Branch Type | Description                                        |
            |-------------|----------------------------------------------------|
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





## 4. 구현 기능

### **✨ 유저**
- 사용자는 회원 정보를 토대로 회원가입을 진행할 수 있습니다. (`CUSTOMER`)
    - 이메일 중복 가입 불가, 관리자 권한으로는 회원가입 불가
- 회원가입 성공 시 이메일, 비밀번호를 이용해 로그인을 하면 토큰이 발급됩니다.
- 사용자는 자신의 정보만을, 관리자는 전체 회원을 조회할 수 있습니다.
- 사용자는 자신의 정보를 관리할 수 있습니다.
    - 이름 및 비밀번호 변경 (기존 비밀번호 체크 후 변경 가능)
- 사용자는 회원 탈퇴를 진행할 수 있습니다.
- 관리자(`MASTER`)는 다른 유저의 권한을 `CUSTOMER, OWNER, MANAGER`로 변경할 수 있습니다.


### **✨ 가게**
- 가게의 `OWNER`로 등록된 사용자는 자신의 가게의 위치 정보(Point)와 함께 가게를 등록하고 관리할 수 있습니다.
    - 가게 정보(위치, 카테고리, 주소, 이름) 수정, 삭제(진행 중인 주문이 있는 가게는 삭제 불가)
    - 가게 등록 후 관리자의 승인을 받으면 (`PENDING → APPROVED`) 상태로 전환이 되어 사용자에게 조회됩니다.
- 사용자는 가게 검색을 통해 승인 상태인 가게를 가게의 평점과 함께 조회할 수 있습니다. (조회 시점 가게 좌표가 특정 서비스 지역에 포함되는 경우 검색 결과에 포함)
- 관리자(MASTER/MANAGER)는 가게 상태값(`PENDING,APPROVED,REJECTED`)을 선택 지정해 모든 가게를 조회할 수 있습니다.
    - 검색 시 서비스 지역을 기준으로 필터링할 수 있습니다.


### **✨ 메뉴**
- 가게의 `OWNER`로 등록된 사용자는 자신의 가게의 메뉴를 관리할 수 있습니다.
    - 메뉴 등록, 정보 수정, 상태(`SOLD_OUT, AVAILABLE`) 수정, 메뉴 삭제
- 사용자는 `메뉴 이름 검색`을 통해 원하는 메뉴를 판매하고 있는 가게 정보를 함께 조회할 수 있습니다.
- 관리자는 SOFT DELETE로 삭제 처리 된 메뉴까지 함께 조회가 가능합니다.


### **✨ 주문**
- 사용자는 자신이 장바구니에 담은 메뉴를 기반으로 주문을 진행할 수 있습니다. (주문 생성 시 `PENDING` 설정)
- 주문 조회를 통해 정보를 조회할 수 있습니다.
    - (주문 상태, 총 금액, 배달 주소, 주문 생성/수락/거절/조리/배달/취소 시각)
- 특정 사용자의 주문 이력을 전체 조회할 수 있습니다.
    - 각 주문에는 주문 상태와 가게 정보, 총 금액 등이 포함됩니다.
- 가게의 `OWNER`는 들어온 모든 주문을 조회하고 관리할 수 있습니다.
    - 주문 진행 상태 변경
    -   * `PENDING → ACCEPTED` : 주문 수락
    * `PENDING → REJECTED` : 주문 거절
    * `ACCEPTED → COOKING` : 조리 시작
    * `COOKING → COOK_COMPLETED` : 조리 완료
    * `COOK_COMPLETED → DELIVERING` : 배달 시작
    * `DELIVERING → DELIVERED` : 배달 완료
    * `PENDING/ACCEPTED → CANCELED` : 주문 취소
- 사용자는 배달이 시작되기 전 상태에서만 주문 취소가 가능하며, 이미 배달중이거나 배달 완료가 된 주문은 취소가 불가능합니다.


### **✨ 결제**
- 사용자는 주문 결제 시 Toss Payments PG사를 통해 결제할 수 있습니다.
    - 결제 요청 시 주문 정보와 결제 정보를 함께 전송합니다. (성공시 `'SUCCEESS'`, 실패 시 `'FAILED'`로 결제 상태 업데이트하고 사용자에게 전달)
- 결제 취소
    - 사용자가 결제 취소 요청 시, Toss Payments API를 통해 결제 취소 처리를 합니다. (취소 성공 시 결제 상태를 `'CANCELED'`로 업데이트)


### **✨ 리뷰**
- 사용자는 `주문 완료`된 주문 건에 한해서 별점을 포함한 리뷰를 작성하고 관리할 수 있습니다.
    - 리뷰 수정, 삭제, 본인이 작성한 리뷰 조회 포함
    - 동일한 주문에 대해서는 중복 리뷰 작성이 불가능합니다.
- 사용자는 특정 가게 별 리뷰를 조회할 수 있고, 특정 가게에 대해 자신이 작성한 리뷰만을 별도로도 조회할 수 있습니다.


### **✨ 카테고리**
- 관리자(MASTER/MANAGER)는 새 카테고리를 등록하고 관리할 수 있습니다.
    - 카테고리 수정, 삭제 포함
- 모든 사용자는 카테고리 목록을 조회할 수 있습니다.
- 관리자는 SOFT DELETE로 처리된 카테고리까지 함께 조회가 가능합니다.


### **✨ 서비스 지역**
- 서비스 지역 이름과 POLYGON geometry로 서비스 지역 데이터를 생성할 수 있습니다.
    - POLYGON geometry로 특정 geometry가 속해있는지 판별하여 서비스 활성화 상태 결정


### **✨ AI**
- 사용자는 프롬프트를 AI에게 전달해 응답을 받을 수 있습니다.
    - 추후 다른 도메인에서도 AI프롬프트를 선택 사용해 내용 생산성을 높일 수 있는 방향으로 확장성 고려
- Ai-log 전체 조회 및 상세 조회(질문한 내용과 답변 내용등의 로그를 확인할 수 있습니다)


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
│  │              ├─ai
│  │              │  ├─application
│  │              │  │  ├─dto
│  │              │  │  └─exception
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  └─repository
│  │              │  ├─infrastructure
│  │              │  │  ├─ai
│  │              │  │  └─repository
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      └─dto
│  │              │          ├─request
│  │              │          └─response
│  │              ├─category
│  │              │  ├─application
│  │              │  │  └─exception
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  ├─exception
│  │              │  │  └─repository
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      └─dto
│  │              │          ├─request
│  │              │          └─response
│  │              ├─global
│  │              │  ├─common
│  │              │  ├─exception
│  │              │  ├─infrastructure
│  │              │  │  └─config
│  │              │  │      ├─security
│  │              │  │      └─swagger
│  │              │  └─response
│  │              ├─kakaolocal
│  │              │  ├─application
│  │              │  │  ├─dto
│  │              │  │  └─exception
│  │              │  ├─infrastructure
│  │              │  └─presentation
│  │              ├─menu
│  │              │  ├─application
│  │              │  │  ├─dto
│  │              │  │  │  └─request
│  │              │  │  └─exception
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  ├─enums
│  │              │  │  ├─exception
│  │              │  │  └─repository
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      └─dto
│  │              │          └─response
│  │              ├─order
│  │              │  ├─application
│  │              │  │  ├─dto
│  │              │  │  │  └─request
│  │              │  │  └─exception
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  ├─enums
│  │              │  │  ├─exception
│  │              │  │  └─repository
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      └─dto
│  │              │          └─response
│  │              ├─ordermenu
│  │              │  ├─application
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  └─repository
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      └─dto
│  │              │          ├─request
│  │              │          └─response
│  │              ├─payment
│  │              │  ├─application
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  ├─exception
│  │              │  │  └─repository
│  │              │  ├─infrastructure
│  │              │  │  ├─config
│  │              │  │  │  └─payment
│  │              │  │  └─repository
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      └─dto
│  │              │          ├─request
│  │              │          └─response
│  │              ├─review
│  │              │  ├─application
│  │              │  │  ├─dto
│  │              │  │  │  └─request
│  │              │  │  └─exception
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  └─repository
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      └─dto
│  │              │          └─response
│  │              ├─servicearea
│  │              │  ├─application
│  │              │  │  ├─dto
│  │              │  │  │  └─request
│  │              │  │  └─exception
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  └─repository
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      └─dto
│  │              │          └─response
│  │              ├─shop
│  │              │  ├─application
│  │              │  │  ├─dto
│  │              │  │  │  └─request
│  │              │  │  └─exception
│  │              │  ├─domain
│  │              │  │  ├─entity
│  │              │  │  ├─enums
│  │              │  │  ├─exception
│  │              │  │  └─repository
│  │              │  ├─infrastructure
│  │              │  │  └─repository
│  │              │  └─presentation
│  │              │      ├─controller
│  │              │      └─dto
│  │              │          └─response
│  │              └─user
│  │                  ├─application
│  │                  │  ├─dto
│  │                  │  │  └─request
│  │                  │  └─excpetion
│  │                  ├─domain
│  │                  │  ├─entity
│  │                  │  ├─enums
│  │                  │  └─repository
│  │                  ├─infrastructure
│  │                  │  └─repository
│  │                  ├─jwt
│  │                  └─presentation
│  │                      ├─controller
│  │                      └─dto
│  │                          └─response
│  └─resources
└─test
    ├─java
    │  └─com
    │      └─sparta
    │          └─bapzip
    │              ├─category
    │              │  ├─application
    │              │  └─domain
    │              │      └─entity
    │              ├─kakaolocal
    │              │  └─application
    │              ├─menu
    │              │  ├─application
    │              │  └─domain
    │              │      └─entity
    │              ├─order
    │              │  ├─application
    │              │  └─domain
    │              │      └─entity
    │              ├─review
    │              │  ├─application
    │              │  └─domain
    │              │      └─entity
    │              ├─servicearea
    │              │  └─application
    │              ├─shop
    │              │  ├─application
    │              │  └─domain
    │              │      ├─entity
    │              │      └─repository
    │              └─user
    │                  ├─application
    │                  ├─domain
    │                  │  └─entity
    │                  └─presentation
    │                      └─controller
    └─resources

```
</details>



### 📄 API 명세서

[API 명세서 자세히 보기](https://teamsparta.notion.site/27a2dc3ef51481e1b791ca087368d2af?v=27a2dc3ef51481498f04000c69f7fb79)
