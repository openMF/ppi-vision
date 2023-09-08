package org.mifos.visionppi.objects

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parceler

/**
 * Created by Apoorva M K on 29/06/19.
 */

@Parcelize

data class Client constructor(
    var entityId: Int?,
    var entityAccountNo: String?,
    var entityExternalId: String?,
    var entityName: String?,
    var entityType: String?,
    var parentId: Int?,
    var parentName: String?,
    var entityMobileNo: String?,
    var entityStatus: Status?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Status::class.java.classLoader)) {
    }

    companion object : Parceler<Client> {

        override fun Client.write(parcel: Parcel, flags: Int) {
            parcel.writeValue(entityId)
            parcel.writeString(entityAccountNo)
            parcel.writeString(entityExternalId)
            parcel.writeString(entityName)
            parcel.writeString(entityType)
            parcel.writeValue(parentId)
            parcel.writeString(parentName)
            parcel.writeString(entityMobileNo)
            parcel.writeParcelable(entityStatus, flags)
            parcel.writeParcelable(entityStatus, flags)
        }

        override fun create(parcel: Parcel): Client {
            return Client(parcel)
        }
    }
}
