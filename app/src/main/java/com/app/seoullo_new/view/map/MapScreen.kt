package com.app.seoullo_new.view.map

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Util.toColor
import com.app.seoullo_new.view.base.LoadingOverlay
import com.app.seoullo_new.view.ui.theme.colorGridItem7
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // 구글 맵 관련
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition.fromLatLngZoom(LatLng(37.5665, 126.9780), 18f)
    }
    val currentPosition by viewModel.currentLocation.collectAsStateWithLifecycle()
    val currentPositionBounds by viewModel.currentLocationBounds.collectAsStateWithLifecycle()
    LaunchedEffect(
        key1 = currentPosition
    ) {
        currentPosition?.let { location ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(location, 18f),
                1000 // 애니메이션 지속 시간 (밀리초)
            )
        }
    }
    LaunchedEffect(
        key1 = currentPositionBounds
    ) {
        currentPositionBounds?.let { bounds ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, 100),
                1000 // 애니메이션 지속 시간 (밀리초)
            )
        }
    }
    val polyline by viewModel.polyline.collectAsStateWithLifecycle()
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
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = true
            )
        )
    }

    // 팝업
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    // Direction 결과
    val directionState by viewModel.direction.collectAsStateWithLifecycle()

    // BottomSheetScaffold 관련
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = true
        )
    )
    LaunchedEffect(directionState) {
        if (directionState is ApiState.Success) {
            sheetState.bottomSheetState.expand() // Bottom Sheet 열기
        } else {
            sheetState.bottomSheetState.partialExpand() // Bottom Sheet 닫기
        }
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            DirectionBottomSheet(
                viewModel = viewModel,
                directionState = directionState
            )
        },
        sheetPeekHeight = 64.dp
    ) {
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                onMyLocationButtonClick = {
                    viewModel.getCurrentLocation()
                    true
                }
            ) {
                // Walking은 점선, 그 이외는 실선
                Polyline(
                    points = polyline.polyLine,
                    color = if (polyline.transitColor.isNotEmpty()) polyline.transitColor.toColor() else colorGridItem7,
                    width = 10f,
                    startCap = RoundCap(),
                    endCap = RoundCap(),
                    pattern = if (polyline.transitType == stringResource(R.string.travel_mode_walking)) listOf(
                        Dot(), Gap(10f)
                    ) else null
                )
            }

            DirectionSelectButton {
                viewModel.openDirectionSelectDialog()
            }

            // 이전 화면에서 입장 시 팝업 바로 열리도록
            LaunchedEffect(
                key1 = viewModel.latLng.address.isNotEmpty()
            ) {
                viewModel.openDirectionSelectDialog()
            }

            // 팝업
            if (dialogState.isDirectionSelectDialogOpen) {
                // 상태 초기화
                DirectionSelectDialog(
                    viewModel = viewModel
                )
            }

            var lastErrorMessage by remember { mutableStateOf<String?>(null) }
            when (directionState) {
                is ApiState.Loading -> {
                    viewModel.closeDirectionSelectDialog()
                    // API 로딩 처리
                    LoadingOverlay()
                }
                is ApiState.Error -> {
                    // TODO: ERROR Message 구글이 주는 걸로 사용 전환
                    val errorMessage = (directionState as ApiState.Error).message
                    if (lastErrorMessage != errorMessage) {
                        Toast.makeText(context, errorMessage ?: "", Toast.LENGTH_SHORT).show()
                        lastErrorMessage = errorMessage
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun DirectionSelectButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .size(56.dp)
            .padding(8.dp)
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