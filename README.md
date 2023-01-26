# 🥕당근마켓 챌린지 Ver.2

---
#### `프로젝트명` 초기 당근마켓 API 서버 구현
#### `기간` 2022.12 ~ 진행 중

<br>

### 💡지원계기

---
코드스쿼드 수료 후 팀 프로젝트를 진행했었고, 개인 프로젝트를 해보고 싶은 욕심이 생겼습니다. 서버 배포, DB 모델링 등 감을 잃는 부분도 많은 것 같아
더 잊어버리기 전에 지금까지 학습했던 기술 스택을 복습하는 느낌으로 진행하기 좋은 챌린지라고 생각해서 지원하게 되었습니다. 

💁🏻 프로젝트의 가장 큰 목표는 다음과 같습니다.
> 1.Spring Security 학습 후 인증, 인가에 적용
- 지금까지 진행한 프로젝트에서는 Spring Security 프레임워크를 적용하지 않고 Interceptor 를 활용해 직접 사용자의 권한을 검사하거나 인증하는 로직을
    작성했습니다. 이번 프로젝트에서는 Spring Security 의 강력함을 느끼기 위해 따로 학습 후 커스텀한 Authentication 인증을 구현했으며 다중 OAuth 로그인 또한 구현했습니다.
    또한 JWT 방식이 아닌 Session + Cookie 방식으로 로그인을 구현했습니다.
<br>

> 2.GitHub Actions, Docker, AWS EC2, VPC 등을 활용한 자동 빌드 및 배포
- Master 브랜치에 Pull-Request 및 Push 요청이 오면 자동으로 빌드 및 배포를 하기 위해 GitHub Actions 를 사용해 스크립트를 작성했습니다.  
<br>

> 3.Spring Data JPA 활용
- JPA 를 사용하면서 꼭 겪을 수 있는 N + 1 문제가 발생하지 않도록 Fetch Join 과 Betch Size 설정을 적절하게 활용하였습니다. 어느정도 개발이 더 진행되면
    많은 양의 Mock 데이터를 추가하고 성능 테스트를 해볼 예정입니다.
<br>

> 4.DB 모델링
- DB 모델링을 위해 생활코딩 이고잉님의 개념적 모델링, 논리적 모델링을 학습하고 ERD 다이어그램을 작성했습니다.
<br>

> 5.확실하게 기록하기
- 모든 작업 내용을 착실하게 Notion, Bear 등 에디터에 적절하게 기록하고 다음 프로젝트를 위한 초석으로 삼으려고 합니다. 현재 Notion 에 섹터별로 나눠 작성 중입니다.
<br>
<br>

  
### ⚒ 사용 기술 스택

---
#### 1. `BackEnd`
   - Java 11
   - Gradle
   - MySQL 8
   - Spring Boot 2.7.0
   - Spring Data JPA
   - Spring Security
   - Junit5
#### 2. `Devops`
   - AWS EC2, VPC
   - GitHub Actions
   - Docker

#### 3. `Tools`
   - IntelliJ
   - Git, GitHub
   
<br>

### 💡 ERD 다이어그램

---
#### 1. `개념적 데이터베이스 모델링`
- 유저와 상품 (1 : N)
- 유저와 댓글 (1 : N)
- 상품과 댓글 (1 : N)
- 상품과 이미지 (1 : N)
- 카테고리와 상품 (1 : N)
- 유저와 찜 (1 : N)
- 상품과 찜 (1 : N)

<img width="500" alt="개념적 모델링 1 이미지" src="https://user-images.githubusercontent.com/79444040/214768210-1d639ebd-585d-4417-8862-3464163f3a6c.png">
<img width="500" alt="개념적 모델링 2 이미지" src="https://user-images.githubusercontent.com/79444040/214768258-eb9a1d5f-cf7f-4887-80d4-116d9a10649c.png">
<br>

#### 2. `논리적 데이터베이스 모델링`
![당근마켓 논리 ERD](https://user-images.githubusercontent.com/79444040/214768331-918860b5-4075-4f6e-b1c5-b1ca4dfd506f.png)
<br>

### 💡 API 설계

---
`개발 중 계속해서 추가 예정`
<br>


| 이름        | Method | URI                    |
|-----------|--------|------------------------|
| 회원가입 폼    | GET    | /joinForm              |
| 회원가입      | POST   | /join                  |
| 로그인 폼     | GET    | /loginForm             |
| 로그인       | POST   | /login                 |
|           |        |                        |
| 상품 페이지 조회 | GET    | /api/product           |
| 상품 등록     | POST   | /api/product           |
| 단일 상품 조회  | GET    | /api/product/{id}      |
| 상품 수정     | POST   | /api/product/{id}      |
| 상품 삭제     | DELETE | /api/product/{id}      |
|           |        |                        |
| 상품 이미지 수정 | POST   | /api/image/{id}        |
| 상품 이미지 삭제 | DELETE | /api/image/{id}        |
|           |        |                        |
| 상품 좋아요 증감 | POST   | /api/likes/{id}        |
|           |        |                        |
| 내 프로필 조회  | GET    | /api/member/{username} |
| 내 프로필 수정  | POST   | /api/member/{username} |

<br>

### 💡 인프라 구상도

---
![인프라 구상도](https://user-images.githubusercontent.com/79444040/214770560-23950b1b-54b4-474e-94f4-4c8ce32101ac.png)
