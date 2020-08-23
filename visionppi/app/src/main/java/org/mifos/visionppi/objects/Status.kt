package org.mifos.visionppi.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Apoorva M K on 28/06/19.
 */

@Parcelize
data class Status(
    var id: Int,
    var code: String,
    var value: String
) : Parcelable
