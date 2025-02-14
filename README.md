<img width="100" height="100" src="https://github.com/user-attachments/assets/a54ef08a-b276-41bd-9779-6df88884e8db">

# 🌇 Seoullo

서울로는 외국인을 위한 서울 관광 안내 앱입니다.

1인 개발 진행중이며 지속적으로 새로운 기술 및 기능 업데이트 중입니다!😊

## 🛠️ Development
### Language
- Kotlin
### Architecture
- Clean Architecture
- Multi Module Architecture
- MVVM
### Libraries
- Kotlin Coroutine
- Dagger Hilt
- Jetpack Compose
  - Material3
  - Navigation
- AAC(Android Architecture Components)
  - Room DB
  - Paging3
  - LiveData
- Glide
- Lottle Animation
- Ted Permission
- Retrofit2
### API
- Google Directions
- Google Login
- Google Maps
- Google Places(New)
- OpenWeatherMap
- Seoul Open API
## 🗺️ Features
|홈 화면 1|홈 화면 2|홈 화면 3|
|:-----:|:-----:|:-----:|
|<img width="240" src="https://github.com/">|<img width="240" src="https://github.com/">|<img width="240" src="https://github.com/">
* 서울시 공공 데이터로 관광지 안내(Default)
* 추가 기능으로 Google Places API로 위치기반 관광지 검색(1Km 내외)
* 메인 화면에 오늘 날씨, 미세먼지, 추천 리스트, 로그아웃 기능 등의 여러 정보 제공(화면 분할은 ViewPager: Compose)
* 리스트 클릭 시 세부 정보 제공
* Google Directions API로 길찾기 서비스 제공
* Papago API로 번역 기능 제공(예정)
