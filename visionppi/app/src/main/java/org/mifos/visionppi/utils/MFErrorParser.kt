package org.mifos.visionppi.utils

import com.google.gson.Gson
import org.mifos.visionppi.models.mifoserror.MifosError

/**
 * @author yashk2000
 * @since 22/06/2020
 */
object MFErrorParser {
    const val LOG_TAG = "MFErrorParser"
    private val gson = Gson()
    @JvmStatic
    fun parseError(serverResponse: String?): MifosError {
        return gson.fromJson(serverResponse, MifosError::class.java)
    }
}