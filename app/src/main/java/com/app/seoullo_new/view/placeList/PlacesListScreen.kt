package com.app.seoullo_new.view.placeList

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.domain.model.Places
import com.app.domain.model.theme.Language
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.SELECTED_NEARBY_LIST
import com.app.seoullo_new.utils.Constants.SELECTED_TOUR_LIST
import com.app.seoullo_new.utils.Logging
import com.app.seoullo_new.utils.Util.getStringResourceKey
import com.app.seoullo_new.view.base.SeoulloAppBar
import com.app.seoullo_new.view.ui.theme.Color_ERROR
import com.app.seoullo_new.view.util.TravelJsonItemData
import com.app.seoullo_new.view.util.theme.LocalLanguage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.LocationServices
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PlacesListScreen(
    viewModel: PlacesListViewModel = hiltViewModel(),
    travelItem: TravelJsonItemData,
    onNavigationClick: () -> Unit
) {
    Logging.d(travelItem)

    var menuClickedPosition by remember { mutableIntStateOf(SELECTED_TOUR_LIST) }
    val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(LocalContext.current)
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
            PlacesList(viewModel, travelItem, menuClickedPosition)
        }
    }
}


@Composable
fun PlacesList(
    viewModel: PlacesListViewModel,
    travelItem: TravelJsonItemData,
    menuClickedPosition: Int
) {
    when (menuClickedPosition) {
        SELECTED_TOUR_LIST -> {
            viewModel.getPlacesList(travelItem)
            val placesListResult = viewModel.placesListResult2.collectAsLazyPagingItems()
            LazyColumn(contentPadding = PaddingValues(14.dp, 7.dp)) {
                items(
                    count = placesListResult.itemCount,
                    key = { placesListResult.peek(it)?.id ?: "" }
                ) { index ->
                    val item = placesListResult[index]!!
                    PlacesListItem(item, menuClickedPosition)
                }
            }
        }

        SELECTED_NEARBY_LIST -> {
            viewModel.getPlacesNearbyList(
                travelItem,
                if (LocalLanguage.current == Language.ENGLISH) "en" else "ko"
            )
            val placesListResult by viewModel.placesListResult.collectAsState(initial = emptyList())
            LazyColumn(contentPadding = PaddingValues(14.dp, 7.dp)) {
                items(placesListResult) { places ->
                    PlacesListItem(places, menuClickedPosition)
                }
            }
        }
    }
}

@Composable
fun PlacesListItem(
    places: Places,
    menuClickedPosition: Int
) {
    val context = LocalContext.current

    Column(
        Modifier.clickable {
            Toast.makeText(context, places.displayName, Toast.LENGTH_SHORT).show()
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
//                placeHolder = ImageBitmap.imageResource(R.drawable.ic_back)
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
                    .fillMaxSize()
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
        // 장소 설명
        if (menuClickedPosition == SELECTED_NEARBY_LIST) {
            Text(
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.fillMaxSize(),
                text = places.description,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
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
                modifier = Modifier.fillMaxSize(),
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