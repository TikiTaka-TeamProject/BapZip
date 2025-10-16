# 프로젝트 소개

### BapZip



#### - 광화문 근처에서 운영될 음식점들의 배달 주문 관리, 결제, 그리고 주문 내역 관리 기능을 제공하는 플랫폼 개발
#### 
#### 

## 팀 소개
감정보단 논리로 움직이는 6인의 T키타카 조입니다.

- #### **T키타카 조** <br>

| name | role | mbti | 담당 파트 |                                                                       Github                                                                        |
|:----:|:----:|:----:|:-----:|:---------------------------------------------------------------------------------------------------------------------------------------------------:|
| 박용재  |  리더  | INTP |       | <a href="https://github.com/SearchColor"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a> |
| 권용은  |  멤버  | ENTP |       |   <a href="https://github.com/rlooko"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>    |
| 권재원  |  멤버  | ISTP |       |  <a href="https://github.com/ReadAlien"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>  |
| 안소나  |  멤버  | ISTP |       |  <a href="https://github.com/sonaanweb"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>  |
| 오상경  |  멤버  | ISTJ |       |   <a href="https://github.com/osk0521"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>   |
| 정인호  |  멤버  | ISTP |       |    <a href="https://github.com/eNoLJ"><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white"></a>    |
  <br>

## 개발 기간

> 2025.09.26 - 2025.10.17

## 🛠️기술 스택

### 프로그래밍 언어

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">

### 개발 환경

- **IDE** : IntelliJ
- **JDK** : 
- **Framework** : springframework.boot version '3.5.6', Spring Data JPA, Spring Security
- **Library** : Lombok, ...
- **Build Tool** : Gradle
- **Database** : PostgreSQL
- **Tool** : Github & git

## 구현 기능

#### **✨ 유저**

* 회원가입

#### **✨ 스프링 시큐리티**
* 로그인 인증
  * JWT token 을 사용한 인증
* 유저 권한

#### **✨ 메뉴**

#### **✨ 가게**
* 가게 정보 관리 
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

#### **✨ 리뷰**

#### **✨ 카테고리**
* 카테고리 등록

    * MASTER 또는 MANAGER 권한이 있는 사용자는 새 카테고리를 등록할 수 있음
    * 등록 정보: 카테고리 이름(name), 설명(description), 활성 상태(isActive)
* 카테고리 목록 조회

    * 모든 사용자는 삭제되지 않은 카테고리 목록을 조회할 수 있음
    * 반환 정보:

        * 카테고리 ID
        * 카테고리 이름
        * 설명
        * 생성일시(createdAt)
        * 수정일시(updatedAt)
* 전체 카테고리 조회(관리자용)

    * MASTER 또는 MANAGER 권한자는 삭제된 카테고리를 포함한 전체 목록 조회 가능
* 특정 카테고리 상세 조회

    * 카테고리 ID 기준으로 상세 정보 조회 가능
    * 반환 정보:

        * 카테고리 ID
        * 이름
        * 설명
        * 삭제 여부(isDeleted)
        * 활성 상태(isActive)
* 카테고리 수정

    * MASTER 또는 MANAGER 권한자가 수정 가능
    * 수정 가능한 정보: 이름(name), 설명(description), 활성 상태(isActive)
* 카테고리 삭제 (Soft Delete)

    * MASTER 또는 MANAGER 권한자가 삭제 가능
    * 삭제 시 isDeleted 플래그가 true로 변경되고, 실제 데이터는 DB에서 제거되지 않음
    * 삭제 시 deletedAt, deletedBy 기록
    * 이미 삭제된 카테고리를 다시 삭제하려 할 경우 예외 발생
    * 해당 카테고리에 속한 가게가 존재할 경우 삭제 불가

#### **✨ 주소**

#### **✨ AI**

##  ERD

<details>
<summary>ERD</summary>

- [dbdiagram link](해당링크)

![image](해당링크)
</details>

## 📑 API 명세서

<details>
<summary>API 명세서</summary>

- [team notion link](해당링크)
</details>

