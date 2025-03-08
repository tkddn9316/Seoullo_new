<img width="100" height="100" src="https://github.com/user-attachments/assets/a54ef08a-b276-41bd-9779-6df88884e8db">

# 🌇 Seoullo

서울로는 외국인을 위한 서울 관광 안내 앱입니다.

1인 개발 진행중이며 지속적으로 새로운 기술 및 기능 업데이트 중입니다!😊

## 🛠️ Development
### Language
- Kotlin
### Architecture
- Clean Architecture
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
  - WorkManager
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
|로그인 화면|
|:-----:|
|<img width="240" src="https://github.com/user-attachments/assets/b29b7809-0110-4383-b93b-d7a56ec39755">|

|홈 화면 1(메인 화면)|홈 화면 2(여행 목록)|홈 화면 3(설정)|
|:-----:|:-----:|:-----:|
|<img width="240" src="https://github.com/user-attachments/assets/c3177a90-fda3-4644-8a6e-3f51ec2a1018">|<img width="240" src="https://github.com/user-attachments/assets/95d09c92-16a2-40db-81fa-f5ea83053670">|<img width="240" src="https://github.com/user-attachments/assets/b4be51bb-098a-4d3a-85ba-acc913e9fa35">|
* 서울의 현재 날씨, 미세먼지, 기온, 1주일 간 예측 날씨, 서울의 진행 중or예정 축제 포스터, 오늘 본 목록 등 제공
* 시간 및 현재 날씨에 따라서 배경색 자동 변경됨
* 오늘 본 목록
  * 하단의 리스트 세부 화면 입장 시 자동으로 DB 저장 및 WorkManager 실행
  * 메인 화면에 최대 5개까지 표시되며 초과될 경우 더 보기 버튼 제공
  * 다음 날 0시가 되었을 경우 WorkManager로 예약된 DB DELETE 작업 실행
* OpenWeatherMap, Seoul Open API 등으로 현재 서울의 날씨 제공

|여행 리스트 화면|리스트 세부 화면|
|:-----:|:-----:|
|<img width="240" src="https://github.com/user-attachments/assets/6f2f2eb1-18cd-4939-a459-c6f195acba70">|<img width="240" src="https://github.com/user-attachments/assets/784f87ee-abf8-49e0-b776-1e6b2c31967e">|
* 서울시 공공 데이터로 관광지 안내(Default)
* 추가 기능으로 Google Places API로 위치기반 관광지 검색(1Km 내외)
* 리스트 하단 FAB로 정렬 기능 제공(맨 위로 이동, 리뷰 순 정렬)
* 리스트 클릭 시 세부 정보 제공
* 세부 화면에서 길 찾기, 리뷰 등의 정보 제공

|길 찾기 설정|길 찾기 시작|
|:-----:|:-----:|
|<img width="240" src="https://github.com/user-attachments/assets/6e8afaa3-5b75-41ce-92e1-68babf152fdb">|<img width="240" src="https://github.com/user-attachments/assets/608bff4d-6c88-4a81-a560-e6b23cfae2f1">|
* Google Directions API로 길 찾기 서비스 제공
* Google AutoComplete API로 타이핑 시 자동 완성 제공
* Current Location 버튼 클릭 시 Reverse Geocoding API로 현재 주소 자동 입력
* 하단 리스트 클릭 시 해당 경로에 맞는 PolyLine이 지도에 그려짐
