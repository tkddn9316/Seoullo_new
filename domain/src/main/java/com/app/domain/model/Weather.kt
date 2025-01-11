package com.app.domain.model

import com.app.domain.model.common.BaseModel

data class Weather(
    val baseData: Int,
    val baseTime: Int,
    val category: String,
    val fcstDate : Int,
    val fcstTime : Int,
    val fcstValue : String,
    val nx : Int,
    val ny : Int
) : BaseModel()


//{
//  "response": {
//    "header": {
//      "resultCode": "00",
//      "resultMsg": "NORMAL_SERVICE"
//    },
//    "body": {
//      "dataType": "JSON",
//      "items": {
//        "item": [
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "TMP",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "-3",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "UUU",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "1.4",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "VVV",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "-0.2",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "VEC",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "278",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "WSD",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "1.4",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "SKY",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "1",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "PTY",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "0",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "POP",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "0",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "WAV",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "-999",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "PCP",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "강수없음",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "REH",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "50",
//            "nx": 60,
//            "ny": 127
//          },
//          {
//            "baseDate": "20250111",
//            "baseTime": "1100",
//            "category": "SNO",
//            "fcstDate": "20250111",
//            "fcstTime": "1200",
//            "fcstValue": "적설없음",
//            "nx": 60,
//            "ny": 127
//          }
//        ]
//      },
//      "pageNo": 1,
//      "numOfRows": 12,
//      "totalCount": 835
//    }
//  }
//}