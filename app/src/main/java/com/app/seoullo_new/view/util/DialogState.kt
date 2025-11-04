package com.app.seoullo_new.view.util

/**
 * 팝업 상태 관리 Data Class
 */
data class DialogState(
    val isThemeDialogOpen: Boolean = false,
    val isLanguageDialogOpen: Boolean = false,
    val isReviewDetailDialogOpen: Boolean = false,
    val isDirectionSelectDialogOpen: Boolean = false,
    val isLogoutDialogOpen: Boolean = false,
    val isTodayWatchedListDialogOpen: Boolean = false,
    val isDeletePostDialogOpen: Boolean = false,
    val isDeleteCommentDialogOpen: Boolean = false,
    val isDeleteReplyDialogOpen: Boolean = false
)
