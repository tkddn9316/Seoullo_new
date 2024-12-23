package com.app.seoullo_new.view.placesDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.ApiState
import com.app.domain.model.Places
import com.app.domain.model.PlacesDetail
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.view.base.ErrorScreen
import com.app.seoullo_new.view.base.LoadingOverlay
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.app.seoullo_new.view.ui.theme.Color_ERROR
import com.app.seoullo_new.view.util.theme.LocalLanguage
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

@Composable
fun PlaceDetailScreen(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit
) {
    val language = LocalLanguage.current
    val placesState by viewModel.placesState.collectAsStateWithLifecycle()
    val detailState by viewModel.placesDetailState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getPlacesDetail(
            languageCode = language
        )
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
                Logging.e(placesState)
                when (detailState) {
                    is ApiState.Initial -> {}
                    is ApiState.Loading -> {
                        // API 로딩 처리
                        LoadingOverlay()
                    }
                    is ApiState.Success -> {
                        val placesDetail = (detailState as ApiState.Success<PlacesDetail>).data ?: PlacesDetail()
                        PlacesDetailView(
                            viewModel = viewModel,
                            placesDetail = placesDetail,
                            places = placesState
                        )
                    }
                    is ApiState.Error -> {
                        val error = (detailState as ApiState.Error).message
                        ErrorScreen(error ?: "")
                    }
                }
            }
        }
    }
}

@Composable
fun PlacesDetailView(
    viewModel: PlacesDetailViewModel = hiltViewModel(),
    placesDetail: PlacesDetail,
    places: Places
) {
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
                    text = placesDetail.phoneNum,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

//    Box(Modifier.fillMaxSize()) {
//        GoogleMap(
//            cameraPositionState = cameraPositionState,
//            properties = mapProperties,
//            uiSettings = mapUiSettings
//        )
//        Column {
//            Button(onClick = {
//                mapProperties = mapProperties.copy(
//                    isBuildingEnabled = !mapProperties.isBuildingEnabled
//                )
//            }) {
//                Text(text = "Toggle isBuildingEnabled")
//            }
//            Button(onClick = {
//                mapUiSettings = mapUiSettings.copy(
//                    mapToolbarEnabled = !mapUiSettings.mapToolbarEnabled
//                )
//            }) {
//                Text(text = "Toggle mapToolbarEnabled")
//            }
//        }
//    }
//    GoogleMap(
//        googleMapOptionsFactory = {
//            GoogleMapOptions().mapId("MyMapId")
//        }
//    )
}