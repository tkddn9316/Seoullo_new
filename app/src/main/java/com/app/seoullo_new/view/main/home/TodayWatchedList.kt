package com.app.seoullo_new.view.main.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.domain.model.TodayWatchedList
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.ui.theme.Color_ERROR
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun TodayWatchedList(
    viewModel: HomeViewModel = hiltViewModel(),
    watchedOnClick: (places: String, isNearby: String) -> Unit,
    list: List<TodayWatchedList>,
    modifier: Modifier = Modifier,
) {
    Logging.d("TodayWatchedList: $list")

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.today_watched_list),
            color = Color.White,
            fontFamily = notosansFont,
            fontSize = 20.sp
        )

        Spacer(modifier = modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = list.take(5)    // 5개만
            ) { todayWatched ->
                val jsonData = remember { Json.encodeToString(viewModel.toPlaces(todayWatched)) }
                val encodedJson = remember { Uri.encode(jsonData) }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    onClick = { watchedOnClick(encodedJson, todayWatched.isNearby) },
                    modifier = modifier.size(280.dp, 255.dp)
                ) {
                    val requestOptions = RequestOptions()
                        .override(900, 600)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()

                    Column {
                        GlideImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            imageModel = todayWatched.photoUrl.ifEmpty { "" },
                            requestOptions = { requestOptions },
                            contentScale = ContentScale.FillBounds,
                            loading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) },
                            failure = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_seoul_symbol),
                                        modifier = modifier.height(150.dp),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit
                                    )
                                    Text(
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color_ERROR,
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(id = R.string.image_loading_error),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.padding(start = 4.dp, end = 4.dp)
                        ) {
                            // 제목
                            Text(
                                style = MaterialTheme.typography.labelMedium.copy(
                                    textAlign = TextAlign.Start,
                                    color = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1f),
                                text = todayWatched.displayName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,   // 길이가 길 경우 줄임 처리
                                maxLines = 1
                            )
                            // 오픈 여부
                            if (todayWatched.isNearby == "Y") {
                                Text(
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (todayWatched.openNow) Color.Red else Color.Blue,
                                        textAlign = TextAlign.End
                                    ),
                                    modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp),
                                    text = if (todayWatched.openNow) stringResource(id = R.string.open) else stringResource(
                                        id = R.string.close
                                    ),
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // 주소
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.padding(start = 4.dp, end = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 4.dp),
                                tint = Color.White
                            )
                            Text(
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White
                                ),
                                text = todayWatched.address,
                                fontSize = 14.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}