package com.app.seoullo_new.view.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.domain.model.Weather
import com.app.seoullo_new.R
import com.app.seoullo_new.view.ui.theme.notosansFont

@Composable
fun DailyWeatherList(
    modifier: Modifier = Modifier,
    item: Weather.DailyWeather
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White.copy(alpha = 0.2f))
            .height(IntrinsicSize.Min)
            .padding(6.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(2f),
            text = item.month,
            fontFamily = notosansFont,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )

        Icon(
            modifier = modifier.padding(start = 10.dp, end = 3.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_weather_rain),
            contentDescription = null,
            tint = Color.White
        )

        Text(
            text = stringResource(R.string.percent_d, item.rainPercent.toInt()),
            fontFamily = notosansFont,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        WeatherListAnim(
            weatherId = item.dailyWeatherId
        )

        Row (
            modifier = modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = modifier.padding(3.dp, 0.dp),
                text = stringResource(id = R.string.temp_symbol, item.maxTemp),
                fontFamily = notosansFont,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(id = R.string.temp_symbol, item.minTemp),
                fontFamily = notosansFont,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun DailyWeatherListPreview() {
    val mockItem = Weather.DailyWeather(
        dailyWeatherName = "Clear",
        dailyWeatherId = 800,
        rainPercent = 0.0,
        maxTemp = -2.98,
        minTemp = -7.33,
        month = "01-28"
    )

    DailyWeatherList(
        item = mockItem
    )
}