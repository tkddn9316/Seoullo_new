package com.app.seoullo_new.view.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.theme.Language
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.getLanguageTitle
import com.app.seoullo_new.view.main.setting.SettingViewModel
import com.app.seoullo_new.view.util.RadioItem
import com.app.seoullo_new.view.util.theme.LocalLanguage
import com.app.seoullo_new.view.util.theme.LocalThemeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

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
            }
        }

        if (dialogState.isDirectionSelectDialogOpen) {
            DirectionSelectDialog(
                viewModel = viewModel
            )
        }
    }
}

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

@Composable
fun DirectionSelectDialog(
    viewModel: MapViewModel = hiltViewModel()
) {
    AlertDialog(
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.language_title),
                    style = MaterialTheme.typography.titleMedium
                )

            }
        },
        onDismissRequest = viewModel::closeDirectionSelectDialog,
        confirmButton = {
            TextButton(
                onClick = viewModel::closeDirectionSelectDialog,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(stringResource(R.string.btn_close))
            }
        }
    )
}