package com.app.seoullo_new.view.placesDetail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.app.seoullo_new.view.ui.theme.Color_ERROR
import com.app.seoullo_new.view.ui.theme.colorRatingStar
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.util.theme.LocalLanguage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Composable
fun PlaceDetailNearbyScreen(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit
) {
    val placesState by viewModel.placesState.collectAsStateWithLifecycle()
    val detailState by viewModel.placesDetailGoogleState.collectAsStateWithLifecycle()

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
                if (BuildConfig.DEBUG) {
                    viewModel.getFakePlacesDetailGoogle(LocalContext.current)
                } else {
                    viewModel.getPlacesDetailGoogle(
                        if (LocalLanguage.current == Language.ENGLISH) stringResource(R.string.en) else stringResource(
                            R.string.ko
                        )
                    )
                }

                when (detailState) {
                    is ApiState.Initial -> {}
                    is ApiState.Loading -> {}
                    is ApiState.Success -> {
                        val placesDetail =
                            (detailState as ApiState.Success<PlacesDetailGoogle>).data
                                ?: PlacesDetailGoogle()
                        PlacesDetail(placesDetail, placesState)
                    }

                    is ApiState.Error -> {
                        val error = (detailState as ApiState.Error).message
                        error?.let { message ->
                            // 에러 메시지 처리
                            Toast.makeText(
                                LocalContext.current,
                                stringResource(R.string.error_failure_init_list, message),
                                Toast.LENGTH_SHORT
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

@Composable
fun PlacesDetail(
    placesDetail: PlacesDetailGoogle,
    places: Places
) {
    val verticalScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

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
            val placesPosition = remember {
                LatLng(placesDetail.latitude, placesDetail.longitude)
            }
            val cameraPositionState = rememberCameraPositionState()

            LaunchedEffect(placesPosition) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(placesPosition, 19f)
            }

            val mapUiSettings = remember {
                MapUiSettings(mapToolbarEnabled = false)
            }

            val marker = remember {
                MarkerState(position = placesPosition)
            }

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                cameraPositionState = cameraPositionState,
                uiSettings = mapUiSettings
            ) {
                Marker(state = marker)
            }

        }

        // 리뷰 미리보기 목록
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
                    modifier = Modifier
                        .size(180.dp, 220.dp)
                        .clickable {

                        }
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
fun LoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = false) {}
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}