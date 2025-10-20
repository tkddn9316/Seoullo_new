package com.app.seoullo_new.view.placesDetail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.DirectionRequest
import com.app.domain.model.Places
import com.app.domain.model.PlacesDetail
import com.app.domain.model.PlacesDetailReview
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Util.getCurrentDate
import com.app.seoullo_new.view.ui.theme.Color_ERROR
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.app.seoullo_new.view.util.CircularProfileImage
import com.app.seoullo_new.view.util.RatingBar
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun PlacesDetailView(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    placesDetail: PlacesDetail,
    places: Places,
    onDirectionClick: (destination: String) -> Unit
) {
    val selectedReview by viewModel.selectedReview.collectAsStateWithLifecycle()
    selectedReview?.let { review ->
        ReviewDetailDialog(viewModel = viewModel, review = review)
    }

    val uriHandler = LocalUriHandler.current
    val verticalScrollState = rememberScrollState()
    val latLng = LatLng(placesDetail.latitude, placesDetail.longitude)
    val markerState = rememberMarkerState(position = latLng)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition.fromLatLngZoom(latLng, 18f)
    }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoomPreference = 20f, minZoomPreference = 5f)
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(mapToolbarEnabled = false)
        )
    }

    val reviewState by viewModel.reviewState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
    ) {
        val requestOptions = RequestOptions()
            .override(900, 600)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            imageModel = places.photoUrl.ifEmpty { "" },
            requestOptions = { requestOptions },
            contentScale = ContentScale.FillBounds,
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
            Text(
                text = placesDetail.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings
            ) {
                AdvancedMarker(
                    state = markerState
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center      // 중앙 정렬
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    onClick = {
                        val directionRequest = DirectionRequest(
                            lat = placesDetail.latitude,
                            lng = placesDetail.longitude,
                            address = placesDetail.address,
                            placeId = ""    // 구글 전용이라 비워둠
                        )
                        val json = Json.encodeToString(directionRequest)
                        val encodedJson = Uri.encode(json)
                        onDirectionClick(encodedJson)
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Directions,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.direction_title))
                    }
                }
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
                    text = placesDetail.address.ifEmpty { stringResource(R.string.line) },
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
                    text = placesDetail.phoneNum.ifEmpty { stringResource(R.string.line) },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Language,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    modifier = Modifier.clickable(
                        enabled = placesDetail.homepage.isNotEmpty(),
                        onClick = {
                            uriHandler.openUri(placesDetail.homepage)
                        }
                    ),
                    text = placesDetail.homepage.ifEmpty { stringResource(R.string.line) },
                    textDecoration = if (placesDetail.homepage.isNotEmpty()) TextDecoration.Underline else null,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 리뷰 목록
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.review_with_icon),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = notosansFont,
            modifier = Modifier.padding(start = 16.dp),
        )
        when (reviewState) {
            is ApiState.Success -> {
                val list = (reviewState as ApiState.Success).data.orEmpty()

                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = list
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
                                        RatingBar(
                                            rating = item.rating
                                        )
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
            }
            else -> {

            }
        }
    }
}

@Composable
fun ReviewDetailDialog(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    review: PlacesDetailReview
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
                RatingBar(
                    rating = review.rating
                )
                Text(
                    text = getCurrentDate(review.timestamp),
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