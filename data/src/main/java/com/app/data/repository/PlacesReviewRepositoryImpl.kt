package com.app.data.repository

import com.app.data.mapper.mapperToPlacesDetailReview
import com.app.data.utils.Util.asSnapshotFlowList
import com.app.domain.model.PlacesDetailReview
import com.app.domain.repository.PlacesReviewRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class PlacesReviewRepositoryImpl @Inject constructor(
    @Named("reviewsRef") private val reviewsRef: CollectionReference
) : PlacesReviewRepository {
    override fun getDocumentPath(contentName: String): String =
        getDocumentRef(contentName).path

    override fun getReviews(contentName: String): Flow<List<PlacesDetailReview>> =
        getDocumentRef(contentName)
            .collection("review")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .asSnapshotFlowList { it.mapperToPlacesDetailReview() }

    private fun getDocumentRef(contentName: String): DocumentReference =
        reviewsRef.document(contentName)
}