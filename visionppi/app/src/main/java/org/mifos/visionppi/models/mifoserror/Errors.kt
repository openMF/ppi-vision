package org.mifos.visionppi.models.mifoserror

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parceler

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    companion object : Parceler<Errors> {

        override fun Errors.write(parcel: Parcel, flags: Int) {
            parcel.writeString(developerMessage)
            parcel.writeString(defaultUserMessage)
            parcel.writeString(userMessageGlobalisationCode)
            parcel.writeString(parameterName)
        }

        override fun create(parcel: Parcel): Errors {
            return Errors(parcel)
        }
    }
}
