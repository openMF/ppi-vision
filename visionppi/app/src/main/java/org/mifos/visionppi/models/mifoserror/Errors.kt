package org.mifos.visionppi.models.mifoserror

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */

@Parcelize
data class Errors(
    var developerMessage: String? = null,
    var defaultUserMessage: String? = null,
    var userMessageGlobalisationCode: String? = null,
    var parameterName: String? = null
) : Parcelable
