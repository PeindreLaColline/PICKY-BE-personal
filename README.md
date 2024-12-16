# PICKY - 영화 리뷰와 소셜플랫폼

### Getting Started
[피그마 페이지](https://www.figma.com/design/rpAlhiLds5pygwPfPpD4lp/PICKY-%EB%94%94%EC%9E%90%EC%9D%B8-%EC%99%84%EC%84%B1%EB%B3%B8?node-id=0-1&node-type=canvas&t=pwFCyVmMoN1a41le-0)<br />
[요구사항 정의서](https://docs.google.com/spreadsheets/d/1puQoU2lwXWyVLx6mc33PdlVW_YVREmc3yd3hdZeMDHE/edit?usp=sharing)<br />



---
<br />

**서비스 소개**

사용자가 선택한 영화 장르에 맞춰 영화를 추천받고, 영화 정보를 확인하며 리뷰를 남길 수 있는 플랫폼입니다. 또한, 영화를 사랑하는 사람들을 위한 소셜 기능을 제공하여, 특정 영화에 대한 관람평이나 관련 이야기를 다른 사용자들과 자유롭게 공유하고 소통할 수 있는 영화 리뷰 및 소셜 플랫폼 서비스입니다.

---
<br />

**Back-End Team**
| 권예진 | 김채원 | 이승훈 |
|:------:|:------:|:------:|
| <img src="https://avatars.githubusercontent.com/YaeJinKwon" alt="권예진" width="150"> | <img src="https://avatars.githubusercontent.com/PeindreLaColline" alt="김채원" width="150"> | <img src="https://avatars.githubusercontent.com/lsh981127" alt="이승훈" width="150"> |
| [GitHub](https://github.com/YaeJinKwon) | [GitHub](https://github.com/PeindreLaColline) | [GitHub](https://github.com/lsh981127) |

---
<br />

**아키텍처**
![KakaoTalk_Photo_2024-11-20-01-06-52](https://github.com/user-attachments/assets/edd360c5-7d24-49e2-bc59-945193993086)

---
<br />

**디렉토리 구조**

```planeText
 .
├── java
│   └── com
│       └── ureca
│           └── picky_be
│               ├── base
│               │   ├── business  # 비즈니스 계층 (Usecase와 Service 파일 위치)
│               │   │   ├── auth
│               │   │   │   └── dto
│               │   │   ├── board
│               │   │   │   └── dto
│               │   │   │       ├── boardDto
│               │   │   │       ├── commentDto
│               │   │   │       ├── contentDto
│               │   │   │       └── likeDto
│               │   │   ├── email
│               │   │   ├── lineReview
│               │   │   │   └── dto
│               │   │   ├── movie
│               │   │   │   └── dto
│               │   │   ├── notification
│               │   │   │   └── dto
│               │   │   ├── playlist
│               │   │   │   └── dto
│               │   │   └── user
│               │   │       └── dto
│               │   ├── implementation  # 구현 계층(Manager 위치)         
│               │   │   ├── auth
│               │   │   ├── board
│               │   │   ├── content
│               │   │   ├── email
│               │   │   ├── lineReview
│               │   │   │   └── mapper
│               │   │   ├── mapper
│               │   │   ├── movie
│               │   │   ├── notification
│               │   │   ├── playlist
│               │   │   └── user
│               │   ├── persistence  # 영속 계층(Repository 위치)
│               │   │   ├── board
│               │   │   ├── follow
│               │   │   ├── lineReview
│               │   │   ├── movie
│               │   │   ├── movieworker
│               │   │   ├── notification
│               │   │   ├── playlist
│               │   │   └── user
│               │   └── presentation  # 프레젠테이션 계층(Controller 위치)
│               │       └── controller
│               │           ├── admin
│               │           ├── auth
│               │           ├── board
│               │           ├── email
│               │           ├── lineReview
│               │           ├── movie
│               │           ├── notification
│               │           ├── playlist
│               │           └── user
│               ├── config   # Config 관련 디랙토리
│               │   └── oAuth2
│               ├── elasticsearch
│               │   └── document
│               │       └── movie
│               ├── global
│               │   ├── exception
│               │   ├── response
│               │   ├── success
│               │   └── web
│               └── jpa
│                   └── entity
│                       ├── board
│                       ├── config
│                       ├── follow
│                       ├── genre
│                       ├── lineReview
│                       ├── movie
│                       ├── movieworker
│                       ├── notification
│                       ├── platform
│                       ├── playlist
│                       ├── recommend
│                       ├── report
│                       └── user
└── resources
    ├── properties
    └── static
        └── files
            ├── image
            ├── profile
            └── video

```
---
### ERD
![PICKY-ERD (2)](https://github.com/user-attachments/assets/a3426484-77e3-4729-bebb-c71f85675754)
---


### 설치 및 실행 방법

```bash
git clone https://github.com/LG-Uplus-Movie-SNS-PICKY/PICKY-BE.git

도커 실행 후, docker-compose up -d 실행

이후 프로젝트 실행
```

---

### 기능 소개

현재 아직 기능 구현 단계에 있습니다.
