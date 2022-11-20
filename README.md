# IntagRa∫

![로고.png](images/로고.png)

> 인테리어에 관심있는 사람들을 위한 자동 태깅 SNS

개발 기간 2022.10.11 ~ 2022.11.21

# 목차

[1.프로젝트 소개](#프로젝트소개)
[2.주요기능](#주요기능)
[3.개발환경](#개발환경)
[4.기술스택](#기술스택)
[5.시스템 구성도](#시스템구성도)
[6. ERD](#ERD)
[7. 와이어프레임](#와이어프레임)
[8. 팀 소개](#팀소개)

# 프로젝트소개

-   프로젝트명: IntagRa∫
-   서비스 특징: 이미지 분석 & 자동 태깅 SNS 어플리케이션
-   기획 배경
    -   해시태그로 광고, 게시물의 유입률을 높일 수 있는 인스타그램이라는 SNS의 존재
    -   홈 인테리어에 관한 높아지는 사람들의 관심도. 실제로 인스타그램에 홈 인테리어 검색 시 다양한 사진이 나옴
    -   최초 요구사항 명세서에는 강아지, 고양이 인식을 통한 자동 해시태그 생성이었지만 이를 더 확장해서 ‘홈 인테리어’ 타겟으로 기획을 구체화
-   주요 기능
    -   게시글 피드
    -   이미지 분석 & 태그 생성
    -   태그 프리셋
    -   유저, 해시태그 팔로우
-   배포 환경
    -   AWS EC2 Ubuntu 20.04 LTS

# 주요기능

### 1. 게시글

![게시글탐색](images/demo/게시글탐색.gif)

### 2. 이미지 분석 & 태그 추출

![사물인식_태그추천](images/demo/사물인식_태그추천.gif)

### 3. 태그 프리셋

![프리셋관리](images/demo/프리셋관리.gif)

### 4. 유저, 해시태그 팔로우

![팔로우](images/demo/팔로우.gif)

# 기타 기능

**구글 OAuth**

![타이틀](images/demo/타이틀.gif)

**프로필 관리**

![프로필관리](images/demo/프로필관리.gif)

**검색**

![검색](images/demo/검색.gif)

###

# 개발환경

-   Windows (10, 11)
-   AWS Ubuntu 20.04 LTS
-   안드로이드
    -   Android Studio 2021.3.1
    -   테스트 기기 : Galaxy S 10, Galaxy Z Flip 4
-   백엔드
    -   IntelliJ 2022.1
    -   openJDK 11
    -   Spring Boot 2.7.5
    -   Gradle 7.5.1
-   AI
    -   Python
    -   Pytorch
    -   GPU서버
-   데이터베이스
    -   MySQL 5.7
-   인프라
    -   Jenkins 2.361.2
    -   Docker 20.10.12
    -   Nginx 1.18.0

# 기술스택

-   **Back-end**
    ![springboot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
    ![springsecurity](https://img.shields.io/badge/SpringSecurity-6DB33F?logo=SpringSecurity&logoColor=FFFFFF&style=for-the-badge)
    ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JsonWebTokens&logoColor=white)
    -   JPA
-   **Database**

    ![MYSQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

-   **AI**

    ![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=Python&logoColor=white)

    ![Pytorch](https://img.shields.io/badge/Pytorch-EE4C2C?style=for-the-badge&logo=Pytorch&logoColor=white)

-   **Infra**

    ![AWS](https://img.shields.io/badge/AWS-FF9900?style=for-the-badge&logo=amazon-aws&logoColor=white)
    ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
    ![Amazon EC2](https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white)
    ![Jenkins](https://img.shields.io/badge/Jenkins-D24939?logo=Jenkins&logoColor=FFFFFF&style=for-the-badge)
    ![Nginx](https://img.shields.io/badge/Nginx-009639?logo=Nginx&logoColor=000000&style=for-the-badge)

-   **Cooperation**
    ![Jira](https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=JiraSoftware&logoColor=white)
    ![GitLab](https://img.shields.io/badge/GitLab-FEEEEE?style=for-the-badge&logo=GitLab&logoColor=FC6D26)
    ![Mattermost](https://img.shields.io/badge/Mattermost-0058CC?logo=Mattermost&logoColor=FFFFFF&style=for-the-badge)
    ![Notion](https://img.shields.io/badge/Notion-000000?logo=Notion&logoColor=FFFFFF&style=for-the-badge)

# 시스템구성도

![AI_아키](images/아키_AI.png)

![BE_아키](images/아키_BE.png)

# ERD

![erd_3.png](images/intagral_ddl_v0_4.png)

# 와이어프레임

![와이어프레임.png](images/와이어프레임.png)

# 팀 소개 - **그 Tag 사람**

```
👦 이득교: 팀장 / AI

🧑 배준성: Backend, Android, Design

🧔 한유연: Android, CI/CD

🧑 정태윤: Android, CI/CD

🧒 성성민: AI

🧒 강봉민: Backend, Infra
```
