# Seoullo
외국인을 위한 서울 관광 안내 앱(졸업작품 remake)

1인 개발 진행중입니다.

## 사용 기술
* Kotlin 언어로 개발
* Clean Architecture, Dagger Hilt으로 독립성 및 모듈화, DIP
* MVVM, DataBinding 사용하여 UI 로직 분리
* Coroutine, Flow로 비동기 처리
* Jetpack Compose(현재 공부중이며 일부 UI만 우선 적용)
* AAC(Room, Paging3, ViewModel, LiveData, Navigation)

## 기능 소개
* 서울시 공공 데이터로 관광지 안내(Default)
* 추가 기능으로 Google Places API로 위치기반 관광지 검색(1Km 내외)
* Google Directions API로 길찾기 서비스 제공(예정)
* Papago API로 번역 기능 제공(예정)

## 작업 히스토리
* **2024/03/04** Initial commit
* **2024/03/08** Local DB, View Setting
* **2024/03/20** Google 로그인 연동
* **2024/03/26** Intro 화면 개발
* **2024/03/28** Google Places API(주변 검색) 적용 완료
* **2024/04/06** Google Places Photo API 적용 완료
* **2024/04/12** Tour API(서울시 관광지 데이터) 적용 완료
