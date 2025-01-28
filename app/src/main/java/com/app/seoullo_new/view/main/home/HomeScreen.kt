package com.app.seoullo_new.view.main.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.seoullo_new.R
import com.app.seoullo_new.view.ui.theme.Color_Fine_Dust_Bad
import com.app.seoullo_new.view.ui.theme.Color_Fine_Dust_Good
import com.app.seoullo_new.view.ui.theme.Color_Fine_Dust_Normal
import com.app.seoullo_new.view.ui.theme.Color_Fine_Dust_Very_Bad
import com.app.seoullo_new.view.ui.theme.notosansFont
import org.joda.time.DateTime

// 기온, 강수확률, 풍속, 습도, 내일/모래 날씨, 미세먼지 등등...
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // 날씨 결과
    val weatherResult by viewModel.weatherResult.collectAsStateWithLifecycle()
    val backgroundColor by viewModel.homeBackgroundColor.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessages.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Brush.linearGradient(colors = backgroundColor))
                .padding(innerPadding),
            contentPadding = PaddingValues(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(12.dp) // TODO: 나중에 좀더 보완해야할 듯
        ) {
            // 온도
            val temperature = weatherResult.temp
            val feelsTemperature = weatherResult.feelsLike
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WeatherAnim(
                            viewModel = viewModel
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.temp_symbol, temperature),
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    // 체감 온도
                    Text(
                        text = stringResource(id = R.string.feels_like, feelsTemperature),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp),
                            tint = Color.White
                        )
                        Text(
                            text = stringResource(R.string.current_seoul, DateTime.now().toString("yyyy-MM-dd HH:mm:ss")),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = modifier.height(12.dp))
                }
            }
            // 날씨 정보
            item {
                Card(
                    modifier = modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Row (
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DustInfoColumn(
                            modifier = Modifier.weight(0.5f),
                            iconId = R.drawable.ic_weather_dust,
                            title = stringResource(R.string.fine_dust),
                            value = weatherResult.fineDust,
                            max = 151f,
                            progressColors = listOf(
                                0..30 to Color_Fine_Dust_Good,
                                31..80 to Color_Fine_Dust_Normal,
                                81..150 to Color_Fine_Dust_Bad,
                                IntRange(151, Int.MAX_VALUE) to Color_Fine_Dust_Very_Bad
                            )
                        )

                        DustInfoColumn(
                            modifier = Modifier.weight(0.5f),
                            iconId = R.drawable.ic_weather_dust,
                            title = stringResource(R.string.ultra_fine_dust),
                            value = weatherResult.ultraFineDust,
                            max = 101f,
                            progressColors = listOf(
                                0..15 to Color_Fine_Dust_Good,
                                16..50 to Color_Fine_Dust_Normal,
                                51..100 to Color_Fine_Dust_Bad,
                                IntRange(101, Int.MAX_VALUE) to Color_Fine_Dust_Very_Bad
                            )
                        )
                    }
                }
                Spacer(modifier = modifier.height(12.dp))
            }
            item {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier.weight(0.5f)
                    ) {
                        WeatherInfoCard(
                            title = stringResource(R.string.sunrise),
                            icon = R.drawable.ic_weather_sunrise,
                            data = stringResource(R.string.am, weatherResult.sunrise)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = modifier.weight(0.5f)
                    ) {
                        WeatherInfoCard(
                            title = stringResource(R.string.sunset),
                            icon = R.drawable.ic_weather_sunset,
                            data = stringResource(R.string.pm, weatherResult.sunset)
                        )
                    }
                }
                Spacer(modifier = modifier.height(12.dp))
            }
            item {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier.weight(0.5f)
                    ) {
                        WeatherInfoCard(
                            title = stringResource(R.string.humidity),
                            icon = R.drawable.ic_weather_humidity,
                            data = weatherResult.humidity.toString()
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = modifier.weight(0.5f)
                    ) {
                        WeatherInfoCard(
                            title = stringResource(R.string.precipitation),
                            icon = R.drawable.ic_weather_rain,
                            data = weatherResult.precipitation.toString()
                        )
                    }
                }
                Spacer(modifier = modifier.height(12.dp))
            }
            item {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier.weight(0.5f)
                    ) {
                        WeatherInfoCard(
                            title = stringResource(R.string.wind_speed),
                            icon = R.drawable.ic_weather_wind_speed,
                            data = weatherResult.windSpeed.toString()
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = modifier.weight(0.5f)
                    ) {
                        WeatherInfoCard(
                            title = stringResource(R.string.uv),
                            icon = R.drawable.ic_weather_uv,
                            data = weatherResult.uvi.toString()
                        )
                    }
                }
                Spacer(modifier = modifier.height(12.dp))
            }
            // 이후 날씨 정보 리스트
            items(
                items = weatherResult.dailyList
            ) {
                DailyWeatherList(
                    item = it
                )
            }
        }

        if (!errorMessage.isNullOrEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun DustInfoColumn(
    modifier: Modifier = Modifier,
    iconId: Int,
    title: String,
    value: Int,
    max: Float,
    progressColors: List<Pair<IntRange, Color>>,
    trackColor: Color = Color.Gray
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 제목 및 아이콘
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconId),
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = title,
                fontFamily = notosansFont,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }

        Text(
            text = when (value) {
                in progressColors[0].first -> stringResource(R.string.fine_dust_good, value)
                in progressColors[1].first -> stringResource(R.string.fine_dust_normal, value)
                in progressColors[2].first -> stringResource(R.string.fine_dust_bad, value)
                else -> stringResource(R.string.fine_dust_very_bad, value)
            },
            fontFamily = notosansFont,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            modifier = Modifier
                .height(10.dp)
                .padding(6.dp, 0.dp, 6.dp, 0.dp),
            progress = { (value.toFloat() / max).coerceIn(0f, 1f) },
            trackColor = trackColor,
            color = progressColors.firstOrNull { value in it.first }?.second ?: Color.White,
            strokeCap = StrokeCap.Round,
            gapSize = (-15).dp,
            drawStopIndicator = {}
        )
    }
}

@Composable
fun WeatherInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: Int,
    data: String
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = modifier.padding(9.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = title,
                    fontFamily = notosansFont,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }

            Text(
                text = data,
                fontFamily = notosansFont,
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}