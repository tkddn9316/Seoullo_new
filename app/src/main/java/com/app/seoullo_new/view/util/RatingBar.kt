package com.app.seoullo_new.view.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.seoullo_new.view.ui.theme.colorRatingStar

@Composable
fun RatingBar(
    rating: Int
) {
    val maxStars = 5
    val filledStars = rating    // 채워진 별의 개수
    val emptyStars = maxStars - filledStars

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = colorRatingStar,
                modifier = Modifier.size(15.dp)
            )
        }
        // 빈 별
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = colorRatingStar,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
fun RatingBarHalf(
    rating: Double,
    userRatingCount: Int,
    modifier: Modifier = Modifier,
) {
    val maxStars = 5
    val filledStars = rating.toInt()    // 채워진 별의 개수
    val halfStar = rating - filledStars >= 0.5  // 반 별 여부
    val emptyStars = maxStars - filledStars - if (halfStar) 1 else 0 // 빈 별 개수

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = colorRatingStar,
                modifier = Modifier.size(15.dp)
            )
        }
        if (halfStar) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = null,
                tint = colorRatingStar,
                modifier = Modifier.size(15.dp)
            )
        }
        // 빈 별
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = colorRatingStar,
                modifier = Modifier.size(15.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        // 평가 수 표시
        Text(
            text = "$rating ($userRatingCount)",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            fontSize = 13.sp
        )
    }
}