package com.app.seoullo_new.view.main.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.domain.model.TodayWatchedList
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.VALUE_YES
import com.app.seoullo_new.view.util.RatingBarHalf
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun TodayWatchedListThumbnail(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    item: TodayWatchedList,
    watchedOnClick: (places: String, isNearby: String) -> Unit,
) {
    val jsonData = remember { Json.encodeToString(viewModel.toPlaces(item)) }
    val encodedJson = remember { Uri.encode(jsonData) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                viewModel.closeTodayWatchedListDialog()
                watchedOnClick(encodedJson, item.isNearby)
            }
            .padding(top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val requestOptions = RequestOptions()
            .override(900, 600)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        GlideImage(
            modifier = Modifier
                .width(64.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(6.dp)),
            imageModel = item.photoUrl.ifEmpty { "" },
            requestOptions = { requestOptions },
            contentScale = ContentScale.FillBounds,
            loading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) },
            failure = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = modifier.weight(1f),
                        painter = painterResource(id = R.drawable.ic_seoul_symbol),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
            }
        )

        Spacer(modifier = modifier.width(16.dp))

        Column(
            modifier = modifier.weight(1f)
        ) {
            // 제목
            Text(
                text = item.displayName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            // 주소
            Text(
                text = item.address,
                fontSize = 10.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            // 별점
            RatingBarHalf(
                item.rating,
                item.userRatingCount
            )
        }

        Spacer(modifier = modifier.width(16.dp))

        Icon(
            ImageVector.vectorResource(id = R.drawable.ic_setting_arrow_right),
            contentDescription = null
        )
    }
}