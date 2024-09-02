
## 개발 환경
- OS: Android(minSdk:21, targetSdk:31)
- Language: Kotlin
- IDE: Android Studio
- Server: Node.js
- Database: MySQL
- Target Device: Galaxy S22

<br/>

## 어플리케이션 소개(Application Introduction)

스포츠 클라이밍을 즐기는 사람들을 위한 커뮤니케이션 어플리케이션 'Hold On'을 개발했다.
게시판 기능과 크루 기능, 지도 기능 등을 제공한다.
***

### 시작화면 - 회원가입 및 로그인
<p>
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/8af4ed89-2c67-49e2-8c22-15a773eb57d1" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/47633889-7e06-46df-9318-d8458644fb60" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/3e1cc0b9-306b-47bf-b41f-48b782ff045e" height="32%" width="32%">
  <br><br><br>
</p>

#### 기능 설명
- SDK를 활용한 소셜 로그인이 가능하다.
- 소셜 로그인을 완료하면 회원가입 페이지로 이동한다.
- 이미 계정이 있는 경우 회원정보 페이지에서 '기존 아이디로 로그인'이 가능하다.
- 데이터베이스에 user 정보가 있는 경우 '로그인 성공' 메세지와 함께 메인 화면으로 이동한다.

***

### Tab 1 - 게시판
<p>
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/6227bfbd-1390-4a27-8da8-8bf2b6b81328" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/544c96ed-6414-42d4-bedb-ec0d389f7565" height="32%" width="32%">
  <br><br>
</p>

#### 기능 설명
- 클라이밍 커뮤니티 기능을 제공한다
- 리사이클러뷰를 이용해 게시글이 나타나고 , onresume일때마다 서버 db로부터 게시글이 update된다
- 사진을 최대 5장까지 첨부가능하게 하였고, 게시글의 사진은 가로로 스크롤된다
- 세개의 카테고리가 있어서 카테고리 탭을 눌러 원하는 카테고리의 글을 필터링 할 수 있다


***

### Tab 2 - 크루
<p>
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/9f80f7fb-6dec-4eb6-9b15-880c38c4ed21" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/cc1ffeb8-5834-4c92-ba3b-b8e288b81713" height="32%" width="32%">
  <img src="[https://github.com/Naeunnkim/madcamp_week1/assets/128071056/8c1de43a-d616-4efb-a742-849ded395e97](https://github.com/Naeunnkim/madcamp_week1/assets/128071056/69f40b8d-b073-43de-8e25-8c43c501ee96)" height="32%" width="32%">
  <br><br><br>
</p>

#### 기능 설명
- 클라이밍 크루에 가입 할 수 있는 탭이다
- 첫 화면에서는 내가 가입한 그룹이 나타난다
- 그룹 만들기를 하여 그룹을 추가하여 db에 저장할 수 있고 첫화면에 업데이트가 된다
- 그룹을 가입할 수 도 있다

***

### Tab 3 - 글 작성
<p>
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/35817199-1f7e-4082-a84d-99e15b58ffd9" height="32%" width="32%">
  <br>
</p>

#### 기능 설명
- 게시글을 작성할 수 있다. 이미지를 최대 5개까지 첨부할 수 있고 게시를 누르면 db에 업데이트된다

***

### Tab 4 - 지도
<p>
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/bdc94221-0417-41f0-a9a3-98b5ba820af6" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/a1e261ee-c53b-4f88-bb46-dc0ef2efab73" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/f0903c3e-64ff-453b-a1e8-a960a51bed88" height="32%" width="32%">
  <br><br><br>
</p>

#### 기능 설명
- 서울시 클라이밍장에 대한 정보를 제공한다.
- 검색 필터를 통해 지역별 암장에 대한 정보만 따로 볼 수 있도록 구현하였다.
- 상세 페이지에서는 클라이밍센터 사진, 이름, 주소와 방문자 리뷰를 제공한다.
- 리뷰 등록 버튼을 통해 리뷰를 등록할 수 있다.

#### 아쉬운 점
- 카카오맵 api를 활용해 지도에 위치정보를 표시할 수 있도록 하고싶었으나 구현하지 못한 것이 아쉽다.

***

### Tab 5 - 프로필
<p>
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/f90a7766-1141-4469-807b-e85816c0aa29" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/5809b0b0-47d5-4fae-8291-47191facf8ed" height="32%" width="32%">
  <br><br>
</p>

#### 기능 설명
- 프로필 사진과 닉네임, 소속 크루 및 볼더링 등급을 표시한다.
- 설정 탭에서는 닉네임, 크루, 볼더링 등급과 프로필 사진을 변경할 수 있다.
***
