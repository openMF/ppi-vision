package org.mifos.visionppi.utils

import com.google.gson.Gson
import org.mifos.visionppi.models.mifoserror.MifosError

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
object MFErrorParser {
    const val LOG_TAG = "MFErrorParser"
    private val gson = Gson()

    @JvmStatic
    fun parseError(serverResponse: String?): MifosError {
        return gson.fromJson(serverResponse, MifosError::class.java)
    }
}
