package org.mifos.visionppi.api

/**
 * @author yashk2000
 * @since 22/06/2020
 */
class BaseURL {
    val url: String? = null
        get() = field ?: PROTOCOL_HTTPS + API_ENDPOINT + API_PATH

    val defaultBaseUrl: String
        get() = PROTOCOL_HTTPS + API_ENDPOINT

    fun getUrl(endpoint: String): String {
        return endpoint + API_PATH
    }

    companion object {
        const val API_ENDPOINT = "demo.mifos.io"
        const val API_PATH = "/fineract-provider/api/v1/"
        const val PROTOCOL_HTTPS = "https://"
    }
}