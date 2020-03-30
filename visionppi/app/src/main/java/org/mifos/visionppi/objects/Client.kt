package org.mifos.visionppi.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Apoorva M K on 29/06/19.
 */

@Parcelize

data class Client constructor(var entityId: Int,
                              var entityAccountNo: String,
                              var entityExternalId: String,
                              var entityName: String,
                              var entityType: String,
                              var parentId: Int,
                              var parentName: String,
                              var entityMobileNo: String,
                              var entityStatus: Status) : Parcelable