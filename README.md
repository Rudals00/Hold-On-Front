# madcamp_week2

# madcamp_week1: 탭 구조를 활용한 안드로이드 앱 개발
> 4분반 김경민, 김나은

몰입캠프 2주차 공동과제는 서버와 DB를 활용한 어플리케이션을 만드는 것이다.

목적: 서로 함께 공통의 과제를 함으로써, 서버 및 DB를 활용하는 것에 익숙해지기

요구사항: 서버, DB, SDK를 활용한 앱으로, 데이터를 서버와 주고받는 것은 포함.

<br/>

## 팀원

* 한양대학교 컴퓨터소프트웨어학부 김경민
* 한양대학교 컴퓨터소프트웨어학부 김나은


<br/>

## 개발 환경
- OS: Android(minSdk:21, targetSdk:31)
- Language: Java
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
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/274ccc61-ab3b-410e-a2d7-fad89a3e08f3" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/85a4fba3-01fc-4675-a6cf-df217cdd0d0b" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/8c1de43a-d616-4efb-a742-849ded395e97" height="32%" width="32%">
  <br><br><br>
</p>

#### 기능 설명
- 프로그램의 drawable폴더에 저장된 20개의 이미지를 RecyclerView의 GridLayoutManager를 활용해 한 행에 3개씩 보여주는 갤러리 화면이다.
- 각각의 사진을 클릭하면 사진 원본을 보여주는 새로운 Activity로 이동한다. PhotoView 라이브러리를 사용해 해당 이미지를 두 손가락으로 확대 및 축소할 수 있도록 구현하였다.

***

### Tab 2 - 크루
<p>
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/274ccc61-ab3b-410e-a2d7-fad89a3e08f3" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/85a4fba3-01fc-4675-a6cf-df217cdd0d0b" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/8c1de43a-d616-4efb-a742-849ded395e97" height="32%" width="32%">
  <br><br><br>
</p>

#### 기능 설명
- 프로그램의 drawable폴더에 저장된 20개의 이미지를 RecyclerView의 GridLayoutManager를 활용해 한 행에 3개씩 보여주는 갤러리 화면이다.
- 각각의 사진을 클릭하면 사진 원본을 보여주는 새로운 Activity로 이동한다. PhotoView 라이브러리를 사용해 해당 이미지를 두 손가락으로 확대 및 축소할 수 있도록 구현하였다.

***

### Tab 3 - 글 작성
<p>
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/274ccc61-ab3b-410e-a2d7-fad89a3e08f3" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/85a4fba3-01fc-4675-a6cf-df217cdd0d0b" height="32%" width="32%">
  <img src="https://github.com/Naeunnkim/madcamp_week1/assets/128071056/8c1de43a-d616-4efb-a742-849ded395e97" height="32%" width="32%">
  <br><br><br>
</p>

#### 기능 설명
- 프로그램의 drawable폴더에 저장된 20개의 이미지를 RecyclerView의 GridLayoutManager를 활용해 한 행에 3개씩 보여주는 갤러리 화면이다.
- 각각의 사진을 클릭하면 사진 원본을 보여주는 새로운 Activity로 이동한다. PhotoView 라이브러리를 사용해 해당 이미지를 두 손가락으로 확대 및 축소할 수 있도록 구현하였다.

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
- 프로그램의 drawable폴더에 저장된 20개의 이미지를 RecyclerView의 GridLayoutManager를 활용해 한 행에 3개씩 보여주는 갤러리 화면이다.
- 각각의 사진을 클릭하면 사진 원본을 보여주는 새로운 Activity로 이동한다. PhotoView 라이브러리를 사용해 해당 이미지를 두 손가락으로 확대 및 축소할 수 있도록 구현하였다.

***
