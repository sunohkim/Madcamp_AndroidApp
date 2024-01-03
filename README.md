<img src="https://capsule-render.vercel.app/api?type=soft&color=F5A9E1&height=80&section=header&text=🐷윌버%20키우기🐷&fontSize=50&fontColor=FFFFFF"/>


# 윌버 키우기

### 👥 Developers
- 김선오
- 송한이: KAIST 전산학부 21학번

### 💻 Tech Stacks
<img src="https://img.shields.io/badge/AndroidStudio-3DDC84?style=flat-square&logo=AndroidStudio&logoColor=white"/> <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=white"/>
- minSdkVersion: 26
- targetSdkVersion: 34

## 📢 Description

***Summary***
몰입캠프 1주차 안드로이드 앱 제작 프로젝트입니다.
본 프로젝트는 세 개의 탭으로 구성된 안드로이드 앱 개발을 다루고 있습니다.

---

### 📱 MainActivity
***Main Features***
- 간단하고 심플한 스플레시 화면을 만들었습니다.
- 화면 하단의 내비게이션바를 이용해 각 탭의 view로 전환할 수 있습니다.

---

### ☎️ TAP1: Contact
![전화번호부](https://github.com/sunohkim/Madcamp_AndroidApp/assets/112535704/ccade6b6-da92-4740-b9a2-e47c0c888a58)
***Main Features***
- 안드로이드 기기로부터 연락처 데이터를 불러와 이름, 전화번호의 형식으로 나열되게 만들었습니다.
- 연락처는 가나다 순으로 정렬되어 있습니다.
- 화면 상단의 Search 부분을 통해 찾고자 하는 연락처를 검색할 수 있습니다.
- 화면 우상단의 +버튼을 누르면 새로운 연락처를 추가할 수 있습니다.
- 각 연락처 목록의 오른쪽 초록색 전화기 버튼을 누르면 전화할 수 있습니다.
- 각 연락처 목록의 오른쪽 빨간색 삭제 버튼을 누르면 해당 연락처를 삭제할 수 있습니다.

***Technical Description***
- RecyclerView를 이용하여 연락처를 목록 형태로 나열하였습니다.
- Cursor를 통해 안드로이드 기기의 연락처를 불러왔습니다.
- +'버튼을 누르면 연락처를 추가하는 새로운 Activity가 나타납니다. <br/>
  Intent를 이용해 연락처 추가 Activity에서 연락처 목록 Fragment로 신규 입력한 이름, 번호를 불러왔습니다.
- Adapter와 Binding을 이용해 전화 버튼을 누르면 전화를, 삭제 버튼을 누르면 삭제를 할 수 있도록 구현했습니다.

---

### 🖼️ TAP2: Gallery
![갤러리](https://github.com/sunohkim/Madcamp_AndroidApp/assets/112535704/ee1bc291-7408-4d5a-9a8b-df6b28034848)
***Main Features***
- 안드로이드 기기로부터 사진 데이터를 불러와 Pinterest 형태(모든 사진의 가로 길이 동일, 사진 별 비율 유지)로 배열하였습니다.
- 화면 우상단의 카메라 버튼을 누르면 카메라로 연결되어 사진을 찍고 저장할 수 있습니다. <br/>
  찍은 사진은 바로 본 앱의 갤러리에 저장됩니다.
- 화면 우상단의 토글을 누르면 전체 사진을 볼 수 있는 view와 사진 하나씩 볼 수 있는 view 중 선택할 수 있습니다.
- 각 사진 항목을 누르면 화면을 가득 채워 볼 수 있습니다. 스와이프하여 다음/이전 사진으로 이동할 수 있습니다.

***Technical Description***
- 세 개의 LinearLayout과, 각각의 LinearLayout에 RecyclerView를 적용하여 Pinterest 형태의, 모든 사진의 가로 길이를 통일하고 각 사진 별 비율을 유지하는 view를 만들었습니다.
- 2개의 Adapter을 이용하여 Pinterest 형태로 사진을 배열하는 view, 한 장씩 스와이프하며 볼 수 있는 view 2개를 모두 구현하였습니다.
- binding을 이용하여 각 이미지의 URI 정보를 바탕으로 이미지를 출력하였습니다.

---

### 👆 TAP3: Clicker Game
![클리커](https://github.com/sunohkim/Madcamp_AndroidApp/assets/112535704/eb5ea4a2-278c-421c-be94-c45e70a12bec)
***Main Features***
- 윌버(돼지)를 클릭하면 윌버가 밥을 먹습니다.
- 밥을 주는 횟수만큼 Coin의 개수가 증가하며, 윌버는 Level Up을 하며 점점 자라게 됩니다.
- 화면 우하단의 마켓 버튼을 누르면 모자 가게로 이동합니다. 모은 코인을 이용하여 윌버를 위한 모자를 살 수 있습니다.
- 화면 좌상단의 샬롯(거미)의 거미줄을 누르면 샬롯이 윌버가 있는 곳으로 내려가며 샬롯과 윌버의 친밀도가 높아집니다.
- 샬롯과 윌버의 친밀도가 기준점을 넘기지 못하면 게임이 종료했을 때 윌버는 주인에게 잡아먹히게 됩니다. <br/>
  하지만 샬롯과 윌버의 친밀도가 기준점을 넘기면, 샬롯이 "대단한 돼지", "멋진" 이라는 한글을 알려주며 윌버는 살 수 있게 됩니다.

***Technical Description***
- Animation을 이용하여 윌버에게 밥을 주는 모션 및 샬롯이 움직이는 모션을 모두 구현하였습니다.
- 수많은 ClickListener과 Handler을 이용해서 클리커 게임의 각 부분을 모두 조절하였습니다.
- 마켓 버튼을 누르면 모자 가게라는 새로운 Activity가 나타납니다. <br/>
  Intent를 이용해 Fragment와 Activity간 데이터를 주고 받았습니다.
