# PICKY - 영화 리뷰와 소셜플랫폼

### Getting Started
[피그마 페이지](https://www.figma.com/design/rpAlhiLds5pygwPfPpD4lp/PICKY-%EB%94%94%EC%9E%90%EC%9D%B8-%EC%99%84%EC%84%B1%EB%B3%B8?node-id=0-1&node-type=canvas&t=pwFCyVmMoN1a41le-0)<br />

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
 ├── main
    │   ├── java
    │   │   └── com
    │   │       └── ureca
    │   │           └── picky_be
    │   │               ├── PickyBeApplication.java
    │   │               ├── base
    │   │               │   ├── business
    │   │               │   │   └── auth
    │   │               │   │       ├── AuthUseCase.java
    │   │               │   │       ├── JwtAuthService.java
    │   │               │   │       └── dto
    │   │               │   │           └── LoginReq.java
    │   │               │   ├── implementation
    │   │               │   │   └── user
    │   │               │   │       └── UserManager.java
    │   │               │   ├── persistence
    │   │               │   │   └── UserRepository.java
    │   │               │   └── presentation
    │   │               │       └── controller
    │   │               │           └── auth
    │   │               │               └── AuthController.java
    │   │               ├── config
    │   │               │   ├── SecurityConfig.java
    │   │               │   └── SwaggerConfig.java
    │   │               └── jpa
    │   │                   ├── config
    │   │                   │   ├── BaseEntity.java
    │   │                   │   └── JpaConfig.java
    │   │                   └── user
    │   │                       └── User.java
    │   └── resources
    │       └── application.yml
```
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
