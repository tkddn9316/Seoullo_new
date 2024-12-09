package com.app.seoullo_new.view.placesDetail

import android.graphics.PointF
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.ApiState
import com.app.domain.model.Places
import com.app.domain.model.PlacesDetailGoogle
import com.app.domain.model.theme.Language
import com.app.seoullo_new.BuildConfig
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.LoadingOverlay
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.app.seoullo_new.view.ui.theme.Color_ERROR
import com.app.seoullo_new.view.ui.theme.colorRatingStar
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.util.theme.LocalLanguage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PlaceDetailNearbyScreen(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit
) {
    val placesState by viewModel.placesState.collectAsStateWithLifecycle()
    val detailState by viewModel.placesDetailGoogleState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val language = LocalLanguage.current

    LaunchedEffect(Unit) {
        if (BuildConfig.DEBUG) {
            viewModel.getFakePlacesDetailGoogle(context)
        } else {
            viewModel.getPlacesDetailGoogle(
                if (language == Language.ENGLISH) "en" else "ko"
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                SeoulloAppBar(
                    title = viewModel.getTitle(),
                    onNavigationClick = onNavigationClick,
                    showAction = false,
                ) { }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                when (detailState) {
                    is ApiState.Initial -> {}
                    is ApiState.Loading -> {}
                    is ApiState.Success -> {
                        val placesDetail =
                            (detailState as ApiState.Success<PlacesDetailGoogle>).data
                                ?: PlacesDetailGoogle()
                        PlacesDetail(
                            viewModel = viewModel,
                            placesDetail = placesDetail,
                            places = placesState
                        )
                    }

                    is ApiState.Error -> {
                        val error = (detailState as ApiState.Error).message
                        error?.let { message ->
                            Logging.e(message)
                            // 에러 메시지 처리
                            Toast.makeText(
                                LocalContext.current, stringResource(R.string.error_failure_init_list, message), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        // API 로딩 처리
        LoadingOverlay(detailState is ApiState.Loading)
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun PlacesDetail(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    placesDetail: PlacesDetailGoogle,
    places: Places
) {
    val selectedReview by viewModel.selectedReview.collectAsStateWithLifecycle()
    selectedReview?.let { review ->
        ReviewDetailDialog(viewModel = viewModel, review = review)
    }

    val verticalScrollState = rememberScrollState()
    val latLng = LatLng(placesDetail.latitude, placesDetail.longitude)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(latLng, 18.0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
    ) {
        val requestOptions = RequestOptions()
            .override(900, 600)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
        val uriHandler = LocalUriHandler.current

        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = places.displayName,
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = colorRatingStar,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${places.rating}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (places.openNow) Color.Red else Color.Blue
                    ),
                    text = if (places.openNow) stringResource(id = R.string.open) else stringResource(
                        id = R.string.close
                    )
                )
            }
            repeat(places.weekdayDescriptions.size) {
                Text(
                    text = places.weekdayDescriptions[it],
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            NaverMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            cameraPositionState.move(CameraUpdate.scrollBy(
                                PointF(dragAmount.x, dragAmount.y)
                            ))
                        }
                    },
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    isScrollGesturesEnabled = true,
                    isZoomGesturesEnabled = true,
                    isZoomControlEnabled = true
                )
            ) {
                Marker(
                    state = MarkerState(position = latLng)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.NearMe,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = places.address,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = placesDetail.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 리뷰 목록(최대 5개)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = placesDetail.reviews
            ) { item ->
                ElevatedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    onClick = { viewModel.openReviewDetailDialog(item) },
                    modifier = Modifier.size(180.dp, 220.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            CircularProfileImage(item.profilePhotoUrl)
                            Spacer(
                                modifier = Modifier
                                    .width(6.dp)
                            )
                            Column {
                                Text(
                                    text = item.profileName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontFamily = notosansFont,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                RatingBar(item.rating)
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .width(13.dp)
                        )
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = notosansFont,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.google_review_limit_notice)
            )
            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    uriHandler.openUri(placesDetail.reviewsUri)
                }
            ) {
                Text(
                    text = stringResource(R.string.see_more)
                )
            }
        }
    }
}

@Composable
fun CircularProfileImage(imageUrl: String, size: Dp = 40.dp) {
    GlideImage(
        imageModel = imageUrl, // URL 또는 기본값
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        placeHolder = painterResource(R.drawable.ic_user_default),
        error = painterResource(R.drawable.ic_user_default)
    )
}

@Composable
fun RatingBar(
    rating: Int
) {
    val maxStars = 5
    val filledStars = rating    // 채워진 별의 개수
    val emptyStars = maxStars - filledStars

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Filled.Star,
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
    }
}

@Composable
fun ReviewDetailDialog(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    review: PlacesDetailGoogle.Review
) {
    AlertDialog(
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                CircularProfileImage(review.profilePhotoUrl)
                Spacer(
                    modifier = Modifier
                        .height(18.dp)
                )
                Text(
                    text = review.profileName,
                    style = MaterialTheme.typography.titleLarge
                )
                RatingBar(review.rating)
                Text(
                    text = review.relativePublishTimeDescription,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(
                    modifier = Modifier
                        .height(18.dp)
                )
                Text(
                    text = review.text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        onDismissRequest = viewModel::closeReviewDetailDialog,
        confirmButton = {
            TextButton(
                onClick = viewModel::closeReviewDetailDialog,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(stringResource(R.string.btn_close))
            }
        }
    )
}