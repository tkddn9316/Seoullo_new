package com.app.seoullo_new.view

import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import com.app.domain.model.Places
import com.app.seoullo_new.base.BaseComposeActivity
import com.app.seoullo_new.view.ui.theme.Color_Gray500
import com.app.seoullo_new.view.ui.theme.MyText
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    Toast
                        .makeText(this, "aa", Toast.LENGTH_SHORT)
                        .show()
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(corner = CornerSize(4.dp)),
            border = BorderStroke(1.dp, Color_Gray500)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(6.dp)
            ) {
                GlideImage(
                    imageModel = places.photoUrl.ifEmpty { "" },
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(16.dp)),
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
                        Text(text = "image request failed.")
                    }
//                placeHolder = ImageBitmap.imageResource(R.drawable.ic_back)
                )
                Column(
                    modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp)
                ) {
                    // 제목
                    Text(
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxSize(),
                        text = places.displayName,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    // 주소
                    Text(
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Black
                        ),
                        modifier = Modifier.fillMaxSize(),
                        text = places.address,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
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