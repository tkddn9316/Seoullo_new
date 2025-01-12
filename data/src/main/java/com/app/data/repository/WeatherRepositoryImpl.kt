package com.app.data.repository

import com.app.data.mapper.mapperToWeather
import com.app.data.source.WeatherDataSource
import com.app.domain.model.Weather
import com.app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource
) : WeatherRepository {
    override fun getWeather(serviceKey: String): Flow<List<Weather>> {
        return flow {
            val timeDate = timeChangeWithYesterdayHandling()
            weatherDataSource.getWeather(
                serviceKey = serviceKey,
                baseDate = timeDate.first,
                baseTime = timeDate.second
            )
                .collect {
                    emit(mapperToWeather(it))
                }
        }
    }

    private fun timeChangeWithYesterdayHandling(): Pair<String, String> {
        // 현재 시간에 따라 데이터 시간 설정(3시간 마다 업데이트) //
        /**
        시간은 3시간 단위로 조회해야 한다. 안그러면 정보가 없다고 뜬다.
        0200, 0500, 0800 ~ 2300까지
         **/

        val now = DateTime.now()
        val dateFormat = DateTimeFormat.forPattern("yyyyMMdd")
        val adjustedTime = when (val currentTime = now.toString("HHmm")) {
            in "0200".."0400" -> "0200"
            in "0500".."0700" -> "0500"
            in "0800".."1000" -> "0800"
            in "1100".."1300" -> "1100"
            in "1400".."1600" -> "1400"
            in "1700".."1900" -> "1700"
            in "2000".."2200" -> "2000"
            else -> {
                // 23시 데이터를 사용할 수 없는 새벽 시간대 처리
                if (currentTime < "0200") {
                    val yesterday = now.minusDays(1)
                    return Pair(yesterday.toString(dateFormat), "2300")
                }
                "2300"
            }
        }

        return Pair(now.toString(dateFormat), adjustedTime)
    }
}