package org.mifos.visionppi.objects

data class SurveySubmissionBody(
    var userId: Int,
    var clientId: Int,
    var createdOn: String,
    var scorecardValues: List<ScoreCardValue>
)
