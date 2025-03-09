# 22-5-team7-Server

# 🥕 와플마켓

이 프로젝트는 온라인 중고거래 및 커뮤니티 플랫폼인 "당근 마켓"을 클론한 프로젝트입니다. 기존의 중고거래 기능에 더해 **경매 기능**을 추가하여 사용자들이 물품을 경매 형식으로 거래할 수 있도록 확장했습니다. 이 프로젝트는 필수 스펙과 권장 스펙을 충족하며, 새로운 기능인 경매를 통해 사용자 경험을 향상시켰습니다. [서비스 바로가기](https://toykarrot.shop/)

---

## 👨‍💼 팀원 소개

- **김정훈**: 소셜 로그인, 환경 설정, 보안 설정
- **박원석**: aws 담당, 마이페이지, 채팅, 경매
- **이준용**: 회원가입, 로그인, 중고거래, 동네생활, 이미지

### 프론트엔드 팀 구성

|           김채민 (GlassyFoozle)           | 허용재 (Quad72) |
| :---------------------------------------: | :-------------: |
| 로그인, 동네생활, 프로필 관련 페이지 담당 |   중고거래, 채팅, 경매 관련 페이지 담당   |

---

## ⚙ 기술 스택
## BackEnd
<div>
<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">    
</div>

### CI/CD
<div>
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> 
<img src="https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white">
</div>


## FrontEnd
- 개발 언어
<img src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white"/>

- 프레임워크 및 라이브러리


<img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB"/> <img src="https://img.shields.io/badge/Yarn-2C8EBB?style=for-the-badge&logo=yarn&logoColor=white"/> <img src="https://img.shields.io/badge/Vite-B73BFE?style=for-the-badge&logo=vite&logoColor=FFD62E"/> 

- Linter

<img src="https://img.shields.io/badge/eslint-3A33D1?style=for-the-badge&logo=eslint&logoColor=white"/>

---

## 🛠 클론 코딩 필수 스펙

### <span>**필수 스펙**</span>

- **회원가입 / 로그인 / 소셜 로그인**
    - 닉네임, 아이디, 비밀번호, 이메일을 통한 회원가입
    - 소셜 로그인 (Google, Kakao, Naver) 지원
- **유저 계정 페이지**
    - 프로필 수정 (사진, 닉네임, 동네)
    - 매너온도 확인
    - 판매내역 조회
    - 내 매너 평가 및 거래 후기 조회
- **글 작성 / 댓글 작성**
    - 중고거래 및 동네생활 게시글 작성
    - 댓글 및 댓글 좋아요 기능
- **페이지네이션**
    - 게시글 목록 및 댓글 목록에 페이지네이션 적용
- **AWS 배포**
    - EC2와 S3를 통한 프로젝트 배포

### <span>**권장 스펙**</span>

- **HTTPS 설정**
    - 보안 강화를 위한 HTTPS 적용
- **Github Actions CI/CD**
    - 자동화된 배포 및 테스트를 위한 CI/CD 파이프라인 구축

---

## ✨ 새로운 기능: **경매**

- **경매 물품 올리기**
    - 판매자가 경매 물품을 등록할 수 있습니다.
    - 시작가와 경매 종료 시간을 설정할 수 있습니다.
- **경매 참여**
    - 구매자는 경매에 참여하여 입찰할 수 있습니다.
    - 입찰 가격은 시작가의 5% 배수로 인상 가능합니다.
- **경매 종료**
    - 제한 시간 내에 가장 높은 가격을 부른 구매자가 낙찰받습니다.
    - 경매 종료시 판매자와 낙찰받은 구매자 간의 채팅방이 생성됩니다.

---

## 💻 전체 기능

### 회원가입
- 닉네임, 아이디, 비밀번호, 이메일을 받아 회원가입을 진행합니다.

### 로그인
- 일반 로그인 및 소셜 로그인을 지원합니다.

### 동네 설정
- 사용자의 동네를 설정합니다. 아직 지역 기반 서비스는 구현하지 못했습니다.

### 홈 (중고거래)
- **물품 올리기**: 판매자가 물품을 등록할 수 있습니다.
- **물품 조회**: 현재 판매중인 모든 물품을 최신순으로 보여줍니다.
- **물품 관심 기능**: 관심 있는 물품을 저장할 수 있습니다.
- **판매자와 채팅**: 구매자와 판매자가 실시간으로 채팅할 수 있습니다.

### 동네 생활
- **글쓰기**: 사용자가 동네 생활 게시글을 작성할 수 있습니다.
- **글 조회**: 게시글을 조회하고 댓글을 작성할 수 있습니다.
- **댓글 좋아요**: 댓글에 좋아요를 누를 수 있습니다.
- **공감**: 게시글에 공감을 표시할 수 있습니다.

### 경매 (새로운 기능)
- **경매 물품 올리기**: 판매자가 경매 물품을 등록합니다.


![경매 글쓰기 화면](https://github.com/user-attachments/assets/9c704363-233e-4989-b4df-d2e49e257d42)
- **경매 참여**: 구매자가 경매에 참여하여 입찰합니다.

![경매 메인](https://github.com/user-attachments/assets/1f044faa-f201-4658-a7e6-1958fbdf8a9d)
<img width="381" alt="경매상세고화질" src="https://github.com/user-attachments/assets/5c0450a7-ff30-483b-aeb7-233c9253ce59" />

- **경매 종료**: 가장 높은 가격을 부른 구매자와 판매자 간의 채팅방이 생성됩니다.

### 채팅
- **채팅 기록 저장**: 물건을 구매할 때 채팅했던 기록이 저장됩니다.
- **실시간 채팅**: WebSocket을 이용하여 실시간 채팅을 구현하였습니다.

  
![채팅홈](https://github.com/user-attachments/assets/be005833-5171-49b4-9956-ca0fe5795943)
![채팅상세2](https://github.com/user-attachments/assets/42ad0132-f713-4a31-bc60-1a1178b1e4bc)
![채팅상세](https://github.com/user-attachments/assets/7ee4e38c-12f6-409f-95b8-a129de530701)


### 나의 당근
- **프로필 수정**: 사진, 닉네임, 동네를 수정할 수 있습니다.
- **매너온도 확인**: 사용자의 매너온도를 확인할 수 있습니다.
- **내 매너 평가 조회**: 다른 사용자로부터 받은 매너 평가를 확인할 수 있습니다.
- **거래 후기 조회**: 거래 후기를 조회할 수 있습니다.
- **관심목록**: 하트를 누른 중고거래 글 목록을 반환합니다.
- **판매내역**: 판매한 물품의 내역을 조회할 수 있습니다.
- **구매내역**: 구매한 물품의 내역을 조회할 수 있습니다.
- **내 동네생활 글**: 동네생활에 작성한 글, 댓글단 글, 공감한 글을 조회할 수 있습니다.

### 타사용자 프로필
- **매너 칭찬**: 다른 사용자의 매너를 칭찬할 수 있습니다.
- **판매물품 목록**: 해당 사용자가 판매 중인 물품을 조회할 수 있습니다.
- **받은 매너 평가**: 해당 사용자가 받은 매너 평가를 확인할 수 있습니다.
- **받은 거래 후기 조회**: 해당 사용자가 받은 거래 후기를 조회할 수 있습니다.

### 이미지
- **AWS S3 활용**: Presigned Url을 발급하고 넘겨줘 프론트에서 AWS S3 버킷에 바로 이미지를 업로드할 수 있도록 했습니다.

### 회원탈퇴
- **회원탈퇴**: 기존 활동 기록은 남습니다. 회원 정보(닉네임, 이메일 등)를 바꾸어 사용자가 누군지 알 수 없게 만듭니다.  
