package com.app.seoullo_new.utils

interface PermissionManager {
    suspend fun checkPermission(vararg permission: String): Boolean
}