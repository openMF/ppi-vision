package org.mifos.visionppi.objects

data class Response(
    var id: Int,
    var text: String,
    var value: Int,
    var sequenceNo: Int,
    var isChecked: Boolean
)
