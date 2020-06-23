package org.mifos.visionppi.models.mifoserror


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author yashk2000
 * @since 22/06/2020
 */

@Parcelize
data class Errors(
        var developerMessage: String? = null,
        var defaultUserMessage: String? = null,
        var userMessageGlobalisationCode: String? = null,
        var parameterName: String? = null
) : Parcelable