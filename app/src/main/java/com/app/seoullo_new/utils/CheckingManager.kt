package com.app.seoullo_new.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.app.seoullo_new.R
import com.gun0912.tedpermission.coroutine.TedPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 네트워크 및 Permission 체크
 */
class CheckingManager @Inject constructor(
    private val context: Context
) : PermissionManager {
    fun checkNetworkState(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    override suspend fun checkPermission(vararg permission: String): Boolean {
        return withContext(Dispatchers.Main) {
            TedPermission.create()
                .setPermissions(*permission)
                .setDeniedMessage(context.getString(R.string.permission_denied))
                .check()
                .isGranted
        }
    }
}