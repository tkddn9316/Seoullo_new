package com.app.seoullo_new.view

import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.domain.model.Places
import com.app.seoullo_new.R
import com.app.seoullo_new.base.BaseComposeActivity
import com.app.seoullo_new.view.ui.theme.Color_Gray500
import com.google.android.gms.location.LocationServices
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesListActivity : BaseComposeActivity<PlacesListViewModel>() {
    override val viewModel: PlacesListViewModel by viewModels()

    @Composable
    override fun Setup() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel.checkPermission(fusedLocationProviderClient)

        PlacesList()
    }

    @Composable
    fun PlacesList() {
        val placesListResult by viewModel.placesListResult.collectAsState(initial = emptyList())
        if (loading) {
            CircularProgress()
        } else {
            LazyColumn(contentPadding = PaddingValues(14.dp, 7.dp)) {
                items(placesListResult) { places ->
                    PlacesListItem(places = places)
                }
            }
        }
    }

    @Composable
    fun PlacesListItem(places: Places) {
        Column(
            Modifier.clickable {
                Toast.makeText(this, places.displayName, Toast.LENGTH_SHORT).show()
            }
        ) {
            GlideImage(
                imageModel = places.photoUrl.ifEmpty {
                    Image(
                        painter = painterResource(id = R.drawable.seoul_symbol),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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
                    Image(
                        painter = painterResource(id = R.drawable.seoul_symbol),
                        contentDescription = null
                    )
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
                    modifier = Modifier.fillMaxSize().weight(1f),
                    text = places.displayName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,   // 길이가 길 경우 줄임 처리
                    maxLines = 1
                )
                // 오픈 여부
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
            // 장소 설명
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
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Seoullo_newTheme {
//        Greeting("Android")
//    }
//}