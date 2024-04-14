package com.app.data.db.entity

abstract class BaseEntity {
    abstract val addr1: String  // 주소
    abstract val addr2: String  // 상세 주소
    abstract val areacode: String   // 지역 코드
    abstract val cat1: String   // 대분류
    abstract val cat2: String   // 중분류
    abstract val cat3: String   // 소분류
    abstract val contentid: String  // 컨텐츠 ID
    abstract val contenttypeid: String  // 컨텐츠 타입 ID
    abstract val createdtime: String    // 등록일
    abstract val dist: String   // 거리(단위: m)
    abstract val firstimage: String     // 대표 이미지(원본)
    abstract val firstimage2: String    // 대표 이미지(썸네일)
    abstract val mapx: String   // GPS X좌표
    abstract val mapy: String   // GPS Y좌표
    abstract val mlevel: String // MAP 레벨
    abstract val modifiedtime: String   // 수정일
    abstract val sigungucode: String    // 시군구 코드
    abstract val tel: String    // 전화번호
    abstract val title: String   // 제목

    var pageNo: Int = 1
}