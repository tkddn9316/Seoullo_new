package com.app.seoullo_new.view.placeList

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.domain.model.Places
import com.app.domain.model.theme.Language
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.SELECTED_NEARBY_LIST
import com.app.seoullo_new.utils.Constants.SELECTED_TOUR_LIST
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.Util.getStringResourceKey
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.app.seoullo_new.view.ui.theme.Color_ERROR
import com.app.seoullo_new.view.ui.theme.colorRatingStar
import com.app.seoullo_new.view.util.TravelJsonItemData
import com.app.seoullo_new.view.util.theme.LocalLanguage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.LocationServices
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun PlacesListScreen(
    viewModel: PlacesListViewModel = hiltViewModel(),
    travelItem: TravelJsonItemData,
    onNavigationClick: () -> Unit,
    onNearbyItemClick: (places: String) -> Unit,
    onItemClick: (places: String) -> Unit
) {
    Logging.d(travelItem)

    var menuClickedPosition by rememberSaveable { mutableIntStateOf(SELECTED_TOUR_LIST) }
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    viewModel.checkPermission(fusedLocationProviderClient)

    val titleResId = getStringResourceKey(travelItem.title)
    val title = stringResource(id = titleResId)

    Scaffold(
        topBar = {
            SeoulloAppBar(
                title = title,
                onNavigationClick = onNavigationClick,
                showAction = true,
            ) { menuClickedPosition = it }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (menuClickedPosition) {
                SELECTED_TOUR_LIST -> {
                    // 페이징 처리 공식문서 참고
                    // https://developer.android.com/develop/ui/compose/lists?hl=ko&_gl=1*1a4v78e*_up*MQ..*_ga*MTEwMzY5NzI1MC4xNzMzMjgwMjk2*_ga_6HH9YJMN9M*MTczMzI5NTEyOS4yLjAuMTczMzI5NTEyOS4wLjAuMTU3MzU2NjEyNA..#large-datasets
                    viewModel.getPlacesList(travelItem)
                    val placesListResult = viewModel.placesListResult2.collectAsLazyPagingItems()

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(contentPadding = PaddingValues(14.dp, 7.dp)) {
                            items(
                                count = placesListResult.itemCount,
                                key = { placesListResult.peek(it)?.id ?: "" }
                            ) { index ->
                                val item = placesListResult[index]!!
                                PlacesListItem(
                                    travelItem.title,
                                    item,
                                    menuClickedPosition,
                                    onItemClick
                                )
                            }
                        }

                        if (placesListResult.loadState.append == LoadState.Loading
                            || placesListResult.loadState.refresh == LoadState.Loading
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        val errorState = placesListResult.loadState.source.refresh as? LoadState.Error
                            ?: placesListResult.loadState.source.append as? LoadState.Error
                        errorState?.let { e ->
                            // 에러 메시지 처리
                            Logging.e(e.error.message ?: "")
                            Toast.makeText(LocalContext.current, stringResource(R.string.error_failure_init_list, "${e.error.message}"), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                SELECTED_NEARBY_LIST -> {
                    if (BuildConfig.DEBUG) {
                        viewModel.getFakePlacesNearbyList(LocalContext.current)
                    } else {
                        viewModel.getPlacesNearbyList(
                            travelItem,
                            if (LocalLanguage.current == Language.ENGLISH) stringResource(R.string.en) else stringResource(R.string.ko)
                        )
                    }
                    val placesListResult by viewModel.placesListResult.collectAsState(initial = emptyList())
                    LazyColumn(contentPadding = PaddingValues(14.dp, 7.dp)) {
                        items(placesListResult) { places ->
                            PlacesListItem(
                                travelItem.title,
                                places,
                                menuClickedPosition,
                                onNearbyItemClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlacesListItem(
    title: String,
    places: Places,
    menuClickedPosition: Int,
    onItemClick: (places: String) -> Unit
) {
    Column(
        Modifier.clickable {
            val json = Json.encodeToString(places)
            val encodedJson = Uri.encode(json)
            onItemClick(encodedJson)
        }
    ) {
        val requestOptions = RequestOptions()
            .override(900, 600)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(10.dp)),
            imageModel = places.photoUrl.ifEmpty { "" },
            requestOptions = { requestOptions },
            contentScale = ContentScale.Crop,
            loading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) },
            failure = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_seoul_symbol),
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
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 제목
            Text(
                style = MaterialTheme.typography.labelMedium.copy(
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .weight(1f),
                text = places.displayName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,   // 길이가 길 경우 줄임 처리
                maxLines = 1
            )
            // 오픈 여부
            if (menuClickedPosition == SELECTED_NEARBY_LIST) {
                Text(
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (places.openNow) Color.Red else Color.Blue,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp),
                    text = if (places.openNow) stringResource(id = R.string.open) else stringResource(
                        id = R.string.close
                    ),
                    fontSize = 14.sp
                )
            }
        }
        if (menuClickedPosition == SELECTED_NEARBY_LIST) {
            // 장소 설명
            Text(
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                text = places.description.ifEmpty { stringResource(getStringResourceKey(title)) },
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            RatingBar(
                places.rating,
                places.userRatingCount
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        // 주소
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp)
            )
            Text(
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                text = places.address,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

    }
    Spacer(modifier = Modifier.height(6.dp))
    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun RatingBar(
    rating: Double,
    userRatingCount: Int,
    modifier: Modifier = Modifier
) {
    val maxStars = 5
    val filledStars = rating.toInt()    // 채워진 별의 개수
    val halfStar = rating - filledStars >= 0.5  // 반 별 여부
    val emptyStars = maxStars - filledStars - if (halfStar) 1 else 0 // 빈 별 개수

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = colorRatingStar,
                modifier = Modifier.size(15.dp)
            )
        }
        if (halfStar) {
            Icon(
                imageVector = Icons.Filled.StarHalf,
                contentDescription = null,
                tint = colorRatingStar,
                modifier = Modifier.size(15.dp)
            )
        }
        // 빈 별
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = colorRatingStar,
                modifier = Modifier.size(15.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        // 평가 수 표시
        Text(
            text = "$rating ($userRatingCount)",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            fontSize = 13.sp
        )
    }
}

/**
 * PREVIEW
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PlacesListItemPreview() {
    val samplePlaces = Places(
        name = "places/ChIJ_e2rpDV9ezURng7-3_dBgq4",
        id = "ChIJ_e2rpDV9ezURng7-3_dBgq4",
        displayName = "잇쇼니",
        address = "대한민국 경기도 부천시 원미구 중동 신흥로 170-1 위브더스테이트 601동 125호 KR",
        description = "일본라면 전문식당",
        openNow = true,
        weekdayDescriptions = listOf("월요일: 오전 11:30 ~ 오후 3:00, 오후 5:00~8:30"),
        rating = 4.3,
        userRatingCount = 385,
        photoUrl = "https://lh3.googleusercontent.com/places/ANXAkqGKyBHt1w-CG0C7CaUjvwBOtBvGXaJT8P3YFUMkToRaXEP9rg1hmQ5Z41iHAmOXIHJdY11xhy-R8CoG3NKy5KjKXsVbOMEJHCA=s4800-w900-h600"
    )

    PlacesListItem(
        title = "Dummy Title",
        places = samplePlaces,
        menuClickedPosition = SELECTED_NEARBY_LIST,
        onItemClick = { }
    )
}