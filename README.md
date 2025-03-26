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
- **글 작성/수정**: 사용자가 동네 생활 게시글을 작성/수정할 수 있습니다.
<img src="https://github.com/user-attachments/assets/c05727b9-9084-469e-bf47-6702b1b744be" width="300"/>

---
- **피드/인기글 목록**: 피드 목록 및 인기글 목록을 반환합니다.
<img src="https://github.com/user-attachments/assets/ea8f95bb-59e5-492a-97bd-aec22a004f9f" width="300"/>
<img src="https://github.com/user-attachments/assets/44606ea3-9ae5-4977-b3ea-8559c38b088d" width="300"/>

---
- **글 조회 및 공감**: 게시글을 조회하고 공감할 수 있습니다.
<img src="https://github.com/user-attachments/assets/cc071b38-2420-4fd7-9e83-258e1a69a685" width="300"/>

---
- **댓글 작성 및 좋아요**: 게시글에 댓글을 작성할 수 있고, 좋아요를 누를 수 있습니다.
<img src="https://github.com/user-attachments/assets/4fbf13b9-bafe-4539-8037-cab65ca7c1c6" width="300"/>

---
- **댓글 수정**: 게시글에 달린 자신의 댓글을 수정할 수 있습니다.
<img src="https://github.com/user-attachments/assets/edb7d071-4e29-45fb-b100-e8613ac18dae" width="300"/>
<img src="https://github.com/user-attachments/assets/2a50de2f-cef0-48a8-96a6-8c5aee1b41f4" width="300"/>
<img src="https://github.com/user-attachments/assets/fc8f4f70-e537-470b-accd-0e235f4ee9fc" width="300"/>

---
- **댓글 삭제**: 게시글에 달린 자신의 댓글을 삭제할 수 있습니다.
<img src="https://github.com/user-attachments/assets/fc8f4f70-e537-470b-accd-0e235f4ee9fc" width="300"/>
<img src="https://github.com/user-attachments/assets/3bf6849f-4ae5-41ff-a245-9a5c740785de" width="300"/>

---

### 경매 (새로운 기능)
<img src="https://github.com/user-attachments/assets/b927cde9-6396-4a98-a323-a436b154da86" width="300"/>

---

- **경매 물품 올리기**: 판매자가 경매 물품을 등록합니다.
<img src="https://github.com/user-attachments/assets/c9153332-ae0b-4c32-a057-f0ccbc86f2e1" width="300"/>

---

- **경매 참여**: 구매자가 경매에 참여하여 입찰합니다. 경매 종료까지 남은 시간과 현재 입찰자 및 입찰가가 websocket을 통해 실시간 반영됩니다.
<img src="https://github.com/user-attachments/assets/e5eb021d-1c13-4212-9c8e-5f6b30260e16" width="300"/>

---

- **경매 종료**: 가장 높은 가격을 부른 구매자와 판매자 간의 채팅방이 생성됩니다.
<img src="https://github.com/user-attachments/assets/47692644-8b0b-4c47-a1f2-ac073fff5d24" width="300"/>

---

### 채팅
- **실시간 채팅**: WebSocket을 이용하여 실시간 채팅을 구현하였습니다. 물건을 구매할 때 채팅했던 기록이 저장됩니다.
<img src="https://github.com/user-attachments/assets/da802a11-4e9a-4d75-99b8-a525d6f78573" width="300"/>
<img src="https://github.com/user-attachments/assets/6567f0f4-d6ea-4a76-ab32-c16172b380bf" width="300"/>

- **물건 상태 변경**: 판매자는 채팅방 내에서 물건 상태를 판매중/예약중/거래완료 중 하나로 변경할 수 있습니다.
<img src="https://github.com/user-attachments/assets/9371a7ab-4902-4a0a-b5fa-4af1c0c87525" width="300"/>

판매자가 거래완료로 상태를 변경하거나 구매자가 거래 완료된 물품에 대한 채팅방에서 후기 보내기를 선택 시 매너 평가 창으로 이동합니다.

---

### 나의 당근
- **프로필 수정**: 사진, 닉네임, 동네를 수정할 수 있습니다.
<img src="https://github.com/user-attachments/assets/e2104ed2-c1ae-4739-afb5-494d419d7d4d" width="300"/>
<img src="https://github.com/user-attachments/assets/45d35780-f55e-444f-a118-c13c3e31bf20" width="300"/>
<img src="https://github.com/user-attachments/assets/395c167d-9c25-4b3d-b532-7df42d400a44" width="300"/>
<img src="https://github.com/user-attachments/assets/16e151e0-218d-47e0-ae93-f252caecdda6" width="300"/>
<img src="https://github.com/user-attachments/assets/e8f3653a-c04d-45f5-92b0-5842d3346f92" width="300"/>

- **매너온도 확인**: 사용자의 매너온도를 확인할 수 있습니다.  
<img src="https://github.com/user-attachments/assets/efb3efb9-f2ce-42eb-ad4b-1511e9081291" width="300"/>

- **내 매너 평가 조회**: 다른 사용자로부터 받은 매너 평가를 확인할 수 있습니다.  
<img src="https://github.com/user-attachments/assets/4dcdcaad-ea98-48aa-888c-e01e25743ea3" width="300"/>

- **거래 후기 조회**: 거래 후기를 조회할 수 있습니다.  
<img src="https://github.com/user-attachments/assets/9cd06a60-5457-4d65-9f5f-37b32d4107be" width="300"/>
<img src="https://github.com/user-attachments/assets/7c1a6bd0-17df-4680-99f1-c19ebeac6e9a" width="300"/>

- **관심목록**: 하트를 누른 중고거래 글 목록을 반환합니다.  
<img src="https://github.com/user-attachments/assets/0875e077-a67c-4fac-b1a9-45c850d4590c" width="300"/>

- **판매내역**: 판매한 물품의 내역을 조회할 수 있습니다.
<img src="https://github.com/user-attachments/assets/ff650d58-e88a-4f8e-85f4-0968e0a58f97" width="300"/>
<img src="https://github.com/user-attachments/assets/976cda50-4f5a-4177-ba42-1a8260541151" width="300"/>

- **구매내역**: 구매한 물품의 내역을 조회할 수 있습니다.  
<img src="https://github.com/user-attachments/assets/97a30e52-0073-4b76-8abe-9cfab3fcda48" width="300"/>

- **내 동네생활 글**: 동네생활에 작성한 글, 댓글단 글, 공감한 글을 조회할 수 있습니다.
<img src="https://github.com/user-attachments/assets/66d5e78e-5edf-42fd-9fcb-ecd766699f66" width="300"/>  
<img src="https://github.com/user-attachments/assets/6fa62d5f-852d-4599-a9e2-92fc309da53f" width="300"/>
<img src="https://github.com/user-attachments/assets/7b6da878-f0a1-420a-944d-ce225c27ccd5" width="300"/>

### 타사용자 프로필
- **판매물품 목록**: 해당 사용자가 판매 중인 물품을 조회할 수 있습니다.  
<img src="https://github.com/user-attachments/assets/d04a17c6-951b-4e04-95e1-96e811001d40" width="300"/>
<img src="https://github.com/user-attachments/assets/3f8b26b3-632d-49c8-bfcd-9ff68522e4be" width="300"/>

- **받은 매너 평가**: 해당 사용자가 받은 매너 평가를 확인할 수 있습니다.  
<img src="https://github.com/user-attachments/assets/c8256c63-6b1c-406c-a5ce-bbd4b8b74deb" width="300"/>

- **받은 거래 후기 조회**: 해당 사용자가 받은 거래 후기를 조회할 수 있습니다.  
<img src="https://github.com/user-attachments/assets/c8e2fe06-1801-4ae7-89f0-a835fa8c532f" width="300"/>

### 후기 남기기
- **구매자측**: 구매자는 거래 완료된 상품의 판매자에 대한 매너 평가 및 후기를 남길 수 있습니다.  
<img src="https://github.com/user-attachments/assets/5cf3a3d7-8d9d-42ef-a67c-e36247dae926" width="300"/>
<img src="https://github.com/user-attachments/assets/551e7530-ac13-4e05-9c09-4c00306c25dd" width="300"/>
<img src="https://github.com/user-attachments/assets/00d2467b-eb94-48e5-9854-a79b8fa43862" width="300"/>

---

- **판매자측**: 판매자도 구매자와 동일한 방식으로 거래 완료된 상품의 구매자에 대한 매너 평가 및 후기를 남길 수 있습니다.  
<img src="https://github.com/user-attachments/assets/d8eab787-b286-4694-b0b3-b9b887f9dacf" width="300"/>
<img src="https://github.com/user-attachments/assets/b66a88bb-a350-4b70-90a9-bad053acb2bf" width="300"/>
<img src="https://github.com/user-attachments/assets/32342a32-f9e5-418b-a640-a2dc3da0134b" width="300"/>
<img src="https://github.com/user-attachments/assets/950f76cf-35b4-4ca7-bad4-882383efd24e" width="300"/>
<img src="https://github.com/user-attachments/assets/12ee8238-4a50-45bf-8088-d3ec6877b031" width="300"/>
<img src="https://github.com/user-attachments/assets/b172d678-bc5b-4b62-b5e7-5a03303d2340" width="300"/>
<img src="https://github.com/user-attachments/assets/240be594-2222-4547-b02b-a6b7816daf3a" width="300"/>
<img src="https://github.com/user-attachments/assets/077ad687-f8d6-46e1-8b96-2692f89e29b2" width="300"/>
<img src="https://github.com/user-attachments/assets/915cf274-ac4c-45db-ac11-82639bdf1139" width="300"/>

### 이미지
- **AWS S3 활용**: Presigned Url을 발급하고 넘겨줘 프론트에서 AWS S3 버킷에 바로 이미지를 업로드할 수 있도록 했습니다.

### 회원탈퇴
<img width="300" src="https://github.com/user-attachments/assets/0b0415ca-4968-48d7-b68b-4672830269ab" />
<img width="300" src="https://github.com/user-attachments/assets/0b6d016d-6794-4ab2-aa18-69ede57b5caa" />

- **회원탈퇴**: 기존 활동 기록은 남습니다. 회원 정보(닉네임, 이메일 등)를 바꾸어 사용자가 누군지 알 수 없게 만듭니다.  
