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
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.domain.model.Places
import com.app.seoullo_new.R
import com.app.seoullo_new.view.base.BaseTitle
import com.app.seoullo_new.view.util.CircularProgress
import com.app.seoullo_new.utils.Constants.SELECTED_NEARBY_LIST
import com.app.seoullo_new.utils.Constants.SELECTED_TOUR_LIST
import com.app.seoullo_new.view.ui.theme.Color_ERROR
import com.app.seoullo_new.view.ui.theme.Color_Gray500
import com.app.seoullo_new.view.ui.theme.Seoullo_newTheme
import com.google.android.gms.location.LocationServices
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun Setup(navController: NavHostController, viewModel: PlacesListViewModel = hiltViewModel()) {

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    viewModel.checkPermission(fusedLocationProviderClient)

    Seoullo_newTheme {
        Surface(
            color = Color.White, // 배경색을 원하는 색상으로 지정
            modifier = Modifier.fillMaxSize() // 전체 화면을 채우도록 설정
        ) {
            Scaffold(
                topBar = { BaseTitle(navController, viewModel) },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        PlacesList(viewModel)
                    }
                }
            )
            CircularProgress()
        }
    }
}


@Composable
fun PlacesList(viewModel: PlacesListViewModel) {
    val menuClickedPosition by viewModel.menuClickedPosition.observeAsState(initial = 0)

    when (menuClickedPosition) {
        SELECTED_TOUR_LIST -> {
            viewModel.getPlacesList()
            val placesListResult = viewModel.placesListResult2.collectAsLazyPagingItems()
            LazyColumn(contentPadding = PaddingValues(14.dp, 7.dp)) {
                items(
                    count = placesListResult.itemCount,
                    key = { placesListResult.peek(it)?.id ?: "" }
                ) { index ->
                    val item = placesListResult[index]!!
                    PlacesListItem(viewModel, item)
                }
            }
        }

        SELECTED_NEARBY_LIST -> {
            viewModel.getPlacesNearbyList()
            val placesListResult by viewModel.placesListResult.collectAsState(initial = emptyList())
            LazyColumn(contentPadding = PaddingValues(14.dp, 7.dp)) {
                items(placesListResult) { places ->
                    PlacesListItem(viewModel, places)
                }
            }
        }
    }
}

@Composable
fun PlacesListItem(viewModel: PlacesListViewModel, places: Places) {
    val context = LocalContext.current

    Column(
        Modifier.clickable {
            Toast.makeText(context, places.displayName, Toast.LENGTH_SHORT).show()
        }
    ) {
        GlideImage(
            imageModel = places.photoUrl.ifEmpty { "" },
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
            loading = {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val indicator = createRef()
                    CircularProgressIndicator(
                        modifier = Modifier.constrainAs(indicator) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }
            },
            failure = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.seoul_symbol),
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
                    color = Color.Black,
                    textAlign = TextAlign.Start
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
            if (viewModel.menuClickedPosition.value!! == SELECTED_NEARBY_LIST) {
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
        if (viewModel.menuClickedPosition.value!! == SELECTED_NEARBY_LIST) {
            Text(
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray
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
                    color = Color.Gray
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
    Divider(color = Color_Gray500)
    Spacer(modifier = Modifier.height(6.dp))
}