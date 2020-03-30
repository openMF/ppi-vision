package org.mifos.visionppi.objects



data class PPISurvey (var id:Int,
                      var componentDatas: List<Component>,
                      var questionDatas : List<Question>,
                      var key : String,
                      var name : String,
                      var description: String,
                      var countryCode : String,
                      var validFrom : String,
                      var validTo : String)