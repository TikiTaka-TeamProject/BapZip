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

| name | role | mbti |    담당 파트     |                                                                       Github                                                                        |
|:----:|:----:|:----:|:------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------:|
| 박용재  |  리더  | INTP |              | <a href="https://github.com/SearchColor"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a> |
| 권용은  |  멤버  | ENTP | 가게, 카테고리 API |   <a href="https://github.com/rlooko"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>    |
| 권재원  |  멤버  | ISTP |              |  <a href="https://github.com/ReadAlien"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>  |
| 안소나  |  멤버  | ISTP |              |  <a href="https://github.com/sonaanweb"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>  |
| 오상경  |  멤버  | ISTJ |              |   <a href="https://github.com/osk0521"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>   |
| 정인호  |  멤버  | ISTP |              |    <a href="https://github.com/eNoLJ"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>    |

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
<img src="https://github.com/user-attachments/assets/8ff5e58f-5673-4d93-afde-f9e98831a6b4" />


</details>

<details>
<summary><strong>컨벤션</strong></summary>

- **Branch**
    - **전략**

      | Branch Type | Description |
            | --- | --- |
      | `dev` | 주요 개발 branch, `main`으로 merge 전 거치는 branch |
      | `feature` | 각자 개발할 branch, 기능 단위로 생성하기, 할 일 issue 등록 후 branch 생성 및 작업 |

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
      📒 Description
      <!-- 진행할 작업을 설명해주세요 -->
      
      📒 To-do
      <!-- 작업을 수행하기 위해 해야할 태스크를 작성해주세요 -->
      [ ] todo1
      
      📒 ETC
      <!-- 특이사항 및 예정 개발 일정을 작성해주세요 -->
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
- 스프링 시큐리티 로그인 인증 / JWT token 을 사용한 인증
- 유저 권한


### **✨ 메뉴**
- 가게의 `OWNER`로 등록된 사용자는 자신의 가게의 메뉴를 관리할 수 있습니다.
    - 메뉴 등록, 정보 수정, 상태(SOLD_OUT, AVAILABLE) 수정, 메뉴 삭제
- 사용자는 `메뉴 이름 검색`을 통해 원하는 메뉴를 판매하고 있는 가게 정보를 함께 조회할 수 있습니다.
- 관리자는 SOFT DELETE로 삭제 처리 된 메뉴까지 함께 조회가 가능합니다.


### **✨ 가게**
* 가게 등록
    * 사용자가 가게를 등록할 수 있음
    * 가게 정보: 이름, 주소, 카테고리, 위치 정보(Point) 포함
    * 가게 등록 후 관리자의 승인(PENDING → APPROVED)을 통해 실제 서비스에 노출
* 가게 상세 조회
    * 특정 가게 ID 기준으로 상세 정보 조회 가능
    * 조회 가능한 정보
        * 가게 ID
        * 가게 이름
        * 주소
        * 현재 상태(PENDING, APPROVED, REJECTED)
        * 가게 소유자 이름
        * 카테고리 이름
        * 평균 평점
* 가게 정보 수정
    * `Owner` 권한이 있는 경우 수정 가능
    * 수정 가능한 정보
        * 이름
        * 주소
        * 카테고리
        * 위치(Point
* 거게 검색 및 상태별 조회
  * Customer 사용자 
    * `Customer` 권한 사용자는 상태가 `APPROVED`인 가게만 조회가능 
    * 검색 시 사용자의 위치 또는 검색한 서비스 지역을 기준으로 검색기준에 포함되는 가게만 결과로 노출 
    * 조회 시점에 가게 좌표가 특정 서비스 지역에 포함되는 경우 검색 결과에 포함 
    * 반환 정보:
      * 가게 ID
      * 가게 이름
      * 주소 
      * 가게 소유자 이름 
      * 카테고리 이름 
      * 서비스 지역 이름(해당되는 경우에만)
      * 평균 평점 
    * Manager / Master 사용자 
      * `Manager` 또는 `Master` 권한 사용자는 가게 상태값을 지정하여 조회 가능 
      * 조회 가능한 상태값:
        * PENDING — 관리자 승인 대기 상태 
        * APPROVED — 승인 완료 상태 
        * REJECTED — 반려 상태 
      * 상태값을 지정하지 않으면 전체 가게 목록이 조회
      * 검색 시 서비스 지역을 기준으로 필터링할 수도 있으며, 관리자는 승인 상태에 관계없이 모든 가게 정보를 확인 가능 
      * 반환 정보:
        * 가게 ID 
        * 가게 이름 
        * 주소 
        * 현재 상태 
        * 가게 소유자 이름 
        * 카테고리 이름
        * 평균 평점
* 가게 삭제 (Soft Delete)
    * `Owner` 권한이 있는 경우 삭제 가능
    * 삭제 시 isDeleted 플래그가 true로 변경되며, 데이터는 실제로 DB에서 제거되지 않음
    * 삭제 시 deletedAt과 deletedBy가 기록됨
    * 이미 삭제된 가게를 삭제하려고 할 경우 예외 발생
    * 진행 중인 주문이 있는 가게는 삭제 불가
### **✨ 주문**

### **✨ 리뷰**

### **✨ 카테고리**
* 카테고리 등록
  * `Manager` 또는 `Master` 권한을 가진 사용자가 새로운 카테고리 등록 가능 
    * 카테고리 정보:
      * 카테고리 ID (자동 생성)
      * 카테고리 이름 
      * 생성 일시 / 수정 일시 
      * 중복된 이름으로는 등록 불가능
* 카테고리 조회 
  * 모든 사용자가 카테고리 목록을 조회 가능 
  * 조회 시 반환 정보:
    * 카테고리 ID 
    * 카테고리 이름
  * 가게 등록 또는 검색 시 해당 카테고리 목록을 기반으로 선택 가능
* 카테고리 수정
  * `Manager` 또는 `Master` 권한 사용자는 기존 카테고리 이름 수정 가능
  * 수정 시:
    * 카테고리 ID는 변경되지 않음 
    * 수정 이력은 기록
    * 중복되는 이름으로는 수정 불가능
* 카테고리 삭제 
  * `Manager` 또는 `Master` 권한 사용자는 카테고리를 삭제 가능
  * 삭제 방식:
    * Soft Delete로 구현되어 DB에서 실제로 제거되지 않으며, isDeleted 값이 true로 변경
    * deletedAt, deletedBy 정보가 기록
    * 삭제된 카테고리는 가게 등록 시 선택할 수 없음 
    * 이미 해당 카테고리를 사용하는 가게가 있는 경우 삭제 시 제약 조건 또는 예외가 발생
### **✨ 주소**

### **✨ AI**



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
    │              ├─shop
    │              │  └─application
    │              │  │       ShopServiceV1Test.java
    │              │  │      
    │              │  └─domain
    │              │      ├─entity
    │              │      │       ShopEntityTest.java
    │              │      └─repository
    │              │              ShopRepositoryTest.java                        
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

