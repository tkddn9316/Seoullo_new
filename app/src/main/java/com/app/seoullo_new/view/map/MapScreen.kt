package com.app.seoullo_new.view.map

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.domain.model.LatLngLiteral
import com.app.seoullo_new.view.util.theme.LocalLanguage

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    destinationLatlng: LatLngLiteral = LatLngLiteral(),
) {
    val language = LocalLanguage.current

//    LaunchedEffect(key1 = destinationLatlng.lat > 0f && destinationLatlng.lng > 0f) {
//        viewModel.getDirection(
//            lat = latlng.lat,
//            lng = latlng.lng,
//            languageCode = if (language == Language.KOREA) "ko" else "en"
//        )
//    }
}