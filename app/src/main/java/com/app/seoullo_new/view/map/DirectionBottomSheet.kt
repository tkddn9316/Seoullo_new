package com.app.seoullo_new.view.map

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.domain.model.Direction
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Util.toColor
import com.app.seoullo_new.view.ui.theme.colorGridItem1
import com.app.seoullo_new.view.ui.theme.colorGridItem3
import com.app.seoullo_new.view.ui.theme.colorGridItem7
import com.app.seoullo_new.view.ui.theme.notosansFont
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.serialization.json.Json

/** 경로 안내 Bottom Sheet */
@Composable
fun DirectionBottomSheet(
    viewModel: MapViewModel = hiltViewModel(),
    directionState: ApiState<Direction>
) {
    val context = LocalContext.current
    if (directionState is ApiState.Success) {
        val directionListState = rememberLazyListState()
        val direction = directionState.data ?: Direction(
            status = "INVALID_REQUEST",
            routes = emptyList()
        )
        if (direction.routes.isNotEmpty()) {
            val item = direction.routes.first()
            val routeList = item.legs.first().steps

            viewModel.setPolyLine(
                encodedPolyLine = item.overviewPolyline
            )
            viewModel.setCurrentLocationBounds(
                lat1 = item.southWestLat, lng1 = item.southWestLng,
                lat2 = item.northEastLat, lng2 = item.northEastLng,
            )

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.35f),
                state = directionListState,
                contentPadding = PaddingValues(8.dp)
            ) {
                item {
                    Column {
                        Text(
                            text = stringResource(R.string.duration),
                            fontFamily = notosansFont,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.height(IntrinsicSize.Min)
                        ) {
                            Text(
                                text = item.legs.first().duration,
                                fontFamily = notosansFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            VerticalDivider(
                                modifier = Modifier.padding(6.dp, 0.dp, 6.dp, 0.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            Text(
                                text = item.legs.first().distance,
                                fontFamily = notosansFont,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "${item.legs.first().departureTime} ~ ${item.legs.first().arrivalTime}",
                            fontFamily = notosansFont,
                            fontSize = 16.sp,
                            color = colorGridItem7
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
                item {
                    DirectionBottomSheetHeader(
                        item = item.legs.first(),
                        isStart = true
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                itemsIndexed(
                    items = routeList,
                    key = { _, item -> item.polyline }
                ) { index, item ->
                    DirectionBottomSheetItem(
                        index = index,
                        item = item
                    ) {
                        viewModel.setPolyLine(data = it)
                        viewModel.setCurrentLocationBounds(
                            lat1 = it.startLocation.lat, lng1 = it.startLocation.lng,
                            lat2 = it.endLocation.lat, lng2 = it.endLocation.lng,
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                item {
                    DirectionBottomSheetHeader(
                        item = item.legs.first(),
                        isStart = false
                    )
                }
            }
        } else {
            Toast.makeText(
                context,
                stringResource(R.string.error_failure_init_list, direction.status),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun DirectionBottomSheetHeader(
    modifier: Modifier = Modifier,
    item: Direction.Route.Leg,
    isStart: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp, 12.dp, 8.dp, 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier.width(55.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_direction_location),
                contentDescription = null,
                tint = if (isStart) colorGridItem1 else colorGridItem3
            )
            Spacer(modifier = modifier.height(3.dp))
            Text(
                text = if (isStart) stringResource(R.string.start2) else stringResource(R.string.arrival),
                fontFamily = notosansFont,
                fontSize = 13.sp,
                color = if (isStart) colorGridItem1 else colorGridItem3
            )
        }
        Spacer(modifier = modifier.width(8.dp))
        Text(
            text = if (isStart) item.startAddress else item.endAddress,
            fontFamily = notosansFont,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DirectionBottomSheetItem(
    modifier: Modifier = Modifier,
    index: Int,
    item: Direction.Route.Leg.Step,
    onRouteClick: (direction: Direction.Route.Leg.Step) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .clickable {
                onRouteClick(item)
            }
            .padding(8.dp, 12.dp, 8.dp, 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier.width(55.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (item.travelMode == stringResource(R.string.travel_mode_walking)) {
                    ImageVector.vectorResource(id = R.drawable.ic_direction_walking)
                } else {
                    item.transitDetails?.let {
                        if (it.transitType == stringResource(R.string.transit_type_bus)) {
                            ImageVector.vectorResource(id = R.drawable.ic_direction_bus)
                        } else {
                            ImageVector.vectorResource(id = R.drawable.ic_direction_subway)
                        }
                    } ?: run {
                        ImageVector.vectorResource(id = R.drawable.ic_direction_walking)
                    }
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = modifier.height(3.dp))
            Text(
                text = item.distance,
                fontFamily = notosansFont,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = modifier.width(8.dp))
        Box(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .size(25.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${index + 1}",
                fontFamily = notosansFont,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = modifier.width(4.dp))
        Column(
            modifier = modifier.weight(1f)
        ) {
            Text(
                text = item.instructions,
                fontFamily = notosansFont,
                style = MaterialTheme.typography.bodyMedium
            )
            item.transitDetails?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlideImage(
                        modifier = modifier.size(16.dp),
                        imageModel = it.transitIcon,
                        contentDescription = null,
                    )
                    Spacer(modifier = modifier.width(4.dp))
                    Text(
                        text = it.transitName,
                        fontFamily = notosansFont,
                        color = it.transitColor.toColor(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = "${it.departureStopName} ~ ${it.arrivalStopName}",
                    fontFamily = notosansFont,
                    color = colorGridItem7,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = modifier.width(6.dp))
        Icon(
            ImageVector.vectorResource(id = R.drawable.ic_setting_arrow_right),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun DirectionBottomSheetItemPreview() {
    val context = LocalContext.current
    val jsonString =
        context.assets.open("example_google_direction2.json").bufferedReader().use { it.readText() }
    val fakeDirection = Json.decodeFromString<Direction>(jsonString)
    val item = fakeDirection.routes.first().legs.first().steps[3]

    DirectionBottomSheetItem(
        index = 98,
        item = item
    ) {

    }
}