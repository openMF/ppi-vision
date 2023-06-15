package org.mifos.visionppi.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
object Network {
    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    private fun getNetworkInfo(context: Context): NetworkInfo {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo!!
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info.isConnected
    }
}
