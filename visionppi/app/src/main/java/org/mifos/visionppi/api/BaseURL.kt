package org.mifos.visionppi.api

/**
 * @author HARSH-nith
 * @since 13/07/2022
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
        const val API_ENDPOINT = "mobile.mifos.io"
        const val API_PATH = "/fineract-provider/api/v1/"
        const val PROTOCOL_HTTPS = "https://"
    }
}
