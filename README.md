# PICKY - 영화 리뷰와 소셜플랫폼

### Getting Started
[피그마 페이지](https://www.figma.com/design/rpAlhiLds5pygwPfPpD4lp/PICKY-%EB%94%94%EC%9E%90%EC%9D%B8-%EC%99%84%EC%84%B1%EB%B3%B8?node-id=0-1&node-type=canvas&t=pwFCyVmMoN1a41le-0)<br />
[문서화(Notion)](https://glass-joggers-e59.notion.site/PICKY-13c9fc77f3f6802ab7f1c2ee59b3aa8c?pvs=74)

<br />

**서비스 소개**

사용자가 선택한 영화 장르에 맞춰 영화를 추천받고, 영화 정보를 확인하며 리뷰를 남길 수 있는 플랫폼입니다. 또한, 영화를 사랑하는 사람들을 위한 소셜 기능을 제공하여, 특정 영화에 대한 관람평이나 관련 이야기를 다른 사용자들과 자유롭게 공유하고 소통할 수 있는 영화 리뷰 및 소셜 플랫폼 서비스입니다.

<br />

**Back-End Team**
| 권예진 | 김채원 | 이승훈 |
|:------:|:------:|:------:|
| <img src="https://file.notion.so/f/f/d159176b-8a5a-4fae-a499-a6b9c2e1223d/6e8573c3-0d20-4522-8bd0-36db76fad9a1/KakaoTalk_Photo_2024-09-24-17-29-43.jpeg?table=block&id=3c0f7a39-c2e0-4fa3-8b72-28992f3b8992&spaceId=d159176b-8a5a-4fae-a499-a6b9c2e1223d&expirationTimestamp=1732111200000&signature=4K9M42pvVT1LY2PKbssiA9fGCK6jt2IHPitEJl6NbwA&downloadName=KakaoTalk_Photo_2024-09-24-17-29-43.jpeg" alt="권예진" width="150"> | <img src="https://file.notion.so/f/f/d159176b-8a5a-4fae-a499-a6b9c2e1223d/6e9ad288-5b3d-4794-a019-8a20afa52cce/Capture_decran_2024-11-12_a_13.38.08.png?table=block&id=7211cb08-13dc-4fa7-abfb-0921592e204f&spaceId=d159176b-8a5a-4fae-a499-a6b9c2e1223d&expirationTimestamp=1732111200000&signature=iuV15kjxv0PzmssgpRxIFQCkM6r6ZpemETt3AsKPUPc&downloadName=Capture+d%E2%80%99e%CC%81cran+2024-11-12+a%CC%80+13.38.08.png" alt="김채원" width="150"> | <img src="https://file.notion.so/f/f/d159176b-8a5a-4fae-a499-a6b9c2e1223d/1c215bcb-551a-4067-983c-894e275eea34/Capture_decran_2024-11-12_a_15.02.50.png?table=block&id=e20d81d9-219d-40de-8b28-8cd93a95d1aa&spaceId=d159176b-8a5a-4fae-a499-a6b9c2e1223d&expirationTimestamp=1732111200000&signature=iQaY-trmoc3Eyw6hx05N68ArvYeSYLSVt320BgIq1LY&downloadName=Capture+d%E2%80%99e%CC%81cran+2024-11-12+a%CC%80+15.02.50.png" alt="이승훈" width="150"> |
| [GitHub](https://github.com/YaeJinKwon) | [GitHub](https://github.com/PeindreLaColline) | [GitHub](https://github.com/lsh981127) |

<br />

**아키텍처**

|  |  |  |  |
|-----------------|----------------|----------------|----------------|
| <img src="https://i.namu.wiki/i/EY559r31H-um8uTtptPIbCZoBGxsumSlwEH0T_rA6WmxQq1UwqyAf3cJQJXN7Fv5CoEz0kv5CBXzjkkPU_XWig.svg" alt="TypeScript" width="100"> | <img src="https://github.com/user-attachments/assets/e3b49dbb-981b-4804-acf9-012c854a2fd2" alt="React" width="100"> | <img src="https://ko.vite.dev/logo.svg" alt="Vite" width="100"> | <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSii2UcY9fK5WXXPfa2z7urgqOcq63L5SObJQ&s" alt="Recoil" width="100"> |
| TypeScript | React | Vite | Recoil |
| <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTKIe10L8m6JqDfjl_5BFRTz8yHoowQUgW6cA&s" alt="Emotion" width="100"> | <img src="https://blog.kakaocdn.net/dn/997rV/btsIkARkTej/PdtiBI82EnMzFQjgHkbuI1/img.png" alt="Storybook" width="100"> | <img src="https://t1.kakaocdn.net/kakao_tech/image/2022/06/images/01.png" alt="React Query" width="100"> |  |
| Emotion | Storybook | React Query |  |

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
    │   │               │   ├── implementation
    │   │               │   │   └── user
    │   │               │   │       └── UserManager.java
    │   │               │   ├── persistence
    │   │               │   │   └── UserRepository.java
    │   │               │   └── presentation
    │   │               │       ├── controller
    │   │               │       │   ├── HomeController.java
    │   │               │       │   └── auth
    │   │               │       │       └── AuthController.java
    │   │               │       └── web
    │   │               │           ├── JwtAuthenticationFilter.java
    │   │               │           ├── JwtDto.java
    │   │               │           ├── JwtProperties.java
    │   │               │           └── JwtTokenProvider.java
    │   │               ├── config
    │   │               │   ├── SecurityConfig.java
    │   │               │   └── SwaggerConfig.java
    │   │               └── jpa
    │   │                   ├── config
    │   │                   │   ├── BaseEntity.java
    │   │                   │   └── JpaConfig.java
    │   │                   └── user
    │   │                       └── User.java
```

---

### 설치 및 실행 방법

```bash
git clone https://github.com/LG-Uplus-Movie-SNS-PICKY/PICKY-BE.git
docker-compose up -d
이후 프로젝트 실행
```

---

### 기능 소개

현재 아직 기능 구현 단계에 있습니다.
