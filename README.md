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
* 메인 화면에 오늘 날씨, 미세먼지, 추천 리스트, 로그아웃 기능 등의 여러 정보 제공(화면 분할은 ViewPager: Compose)
* 리스트 클릭 시 세부 정보 제공
* Google Directions API로 길찾기 서비스 제공
* Papago API로 번역 기능 제공(예정)

## 사용 API
* OpenWeatherMap
