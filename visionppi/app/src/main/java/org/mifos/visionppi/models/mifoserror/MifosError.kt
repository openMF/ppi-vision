package org.mifos.visionppi.models.mifoserror

import android.os.Parcelable
import java.util.ArrayList
import kotlinx.android.parcel.Parcelize

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */

@Parcelize
data class MifosError(
    var developerMessage: String? = null,
    var httpStatusCode: String? = null,
    var defaultUserMessage: String? = null,
    var userMessageGlobalisationCode: String? = null,
    var errors: List<Errors> = ArrayList()
) : Parcelable
