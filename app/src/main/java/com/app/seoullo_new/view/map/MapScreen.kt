package com.app.seoullo_new.view.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.seoullo_new.view.util.theme.LocalLanguage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
//    LaunchedEffect(key1 = destinationLatlng.lat > 0f && destinationLatlng.lng > 0f) {
//        viewModel.getDirection(
//            lat = latlng.lat,
//            lng = latlng.lng,
//            languageCode = if (language == Language.KOREA) "ko" else "en"
//        )
//    }
    // 언어
    val language = LocalLanguage.current

    // 구글 맵 관련
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition.fromLatLngZoom(LatLng(37.5665, 126.9780), 18f)
    }
    val currentPosition by viewModel.currentLocation.collectAsStateWithLifecycle()
    LaunchedEffect(currentPosition) {
        currentPosition?.let { location ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(location, 18f),
                1000 // 애니메이션 지속 시간 (밀리초)
            )
        }
    }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true,
                maxZoomPreference = 20f,
                minZoomPreference = 5f
            )
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false,
                myLocationButtonEnabled = true
            )
        )
    }

    // 팝업
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    // Direction 결과
    val directionState by viewModel.direction.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onMyLocationButtonClick = {
                viewModel.getCurrentLocation()
                true
            }
        )
        Column {
            CustomMyLocationButton {
                viewModel.openDirectionSelectDialog()
//                showBottomSheet = true
            }
        }

        // 이전 화면에서 입장 시
        LaunchedEffect(key1 = viewModel.latLng.address.isNotEmpty()) {
            viewModel.openDirectionSelectDialog()
        }

        if (dialogState.isDirectionSelectDialogOpen) {
            DirectionSelectDialog(
                viewModel = viewModel,
                destination = viewModel.latLng.address
            )
        }

        // TODO: dialogState.isDirectionSelectDialogOpen 처럼 변경
//        if (showBottomSheet) {
//            DirectionBottomSheet {
//                showBottomSheet = it
//            }
//        }
    }
}

/** 현재 위치로 이동 */
@Composable
fun CustomMyLocationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .size(56.dp)
            .shadow(8.dp, CircleShape),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = Icons.Default.Directions,
            tint = Color.White,
            contentDescription = "My Location"
        )
    }
}

/** 경로 안내 Bottom Sheet */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectionBottomSheet(
    onClose: (isShowBottomSheet: Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = {
            onClose(false)
        },
        scrimColor = Color.Transparent,
        sheetState = sheetState
    ) {
        // Sheet content
        Button(onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onClose(false)
                }
            }
        }) {
            Text("Hide bottom sheet")
        }
    }
}