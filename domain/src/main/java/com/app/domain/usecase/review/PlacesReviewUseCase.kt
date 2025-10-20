package com.app.domain.usecase.review

import com.app.domain.model.PlacesDetailReview
import com.app.domain.model.User
import com.app.domain.model.common.ApiState
import com.app.domain.repository.PlacesReviewRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class PlacesReviewUseCase @Inject constructor(
    private val repository: PlacesReviewRepository
) {
    fun getReviews(contentName: String): Flow<ApiState<List<PlacesDetailReview>>> = flow {
        emit(ApiState.Loading())
        try {
            repository.getReviews(
                contentName = contentName
            ).collect {
                emit(ApiState.Success(it))
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is IOException -> "Network Error: ${e.message}"
                is JsonSyntaxException -> "Parsing error: Received non-JSON response (possibly HTML)."
                else -> "Exception: ${e.message}"
            }
            emit(ApiState.Error(errorMessage))
        }
    }

    // TODO: addReview 함수 추후 작업 필요(일단 게시판 작업부터...)
//    fun addReview(
//        userInfo: User,
//        contentName: String,
//        reviewText: String
//    ): Flow<ApiState<Unit>> = flow {
//        emit(ApiState.Loading())
//        try {
//            val review = PlacesDetailReview(
//
//            )
//            repository.addReview(
//                contentName = contentName,
//                review = review
//            ).collect {
//                emit(ApiState.Success())
//            }
//        } catch (e: Exception) {
//            val errorMessage = when (e) {
//                is IOException -> "Network Error: ${e.message}"
//                is JsonSyntaxException -> "Parsing error: Received non-JSON response (possibly HTML)."
//                else -> "Exception: ${e.message}"
//            }
//            emit(ApiState.Error(errorMessage))
//        }
//    }
}