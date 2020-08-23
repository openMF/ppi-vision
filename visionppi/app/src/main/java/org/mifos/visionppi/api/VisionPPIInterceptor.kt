/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package org.mifos.visionppi.api

import android.text.TextUtils
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author yashk2000
 * @since 22/06/2020
 */
class VisionPPIInterceptor(private val tenant: String, private val authToken: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val chainRequest = chain.request()
        val builder = chainRequest.newBuilder()
                .header(HEADER_TENANT, tenant)
        if (!TextUtils.isEmpty(authToken)) {
            builder.header(HEADER_AUTH, authToken)
        }
        val request = builder.build()
        return chain.proceed(request)
    }

    companion object {
        const val HEADER_TENANT = "Fineract-Platform-TenantId"
        const val HEADER_AUTH = "Authorization"
        const val DEFAULT_TENANT = "mobile"
    }
}
