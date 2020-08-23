package org.mifos.visionppi.objects

data class Question(
    var id: Int,
    var responseDatas: List<Response>,
    var componentKey: String,
    var key: String,
    var text: String,
    var description: String,
    var sequenceNo: Int
)
