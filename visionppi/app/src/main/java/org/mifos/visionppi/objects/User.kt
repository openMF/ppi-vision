package org.mifos.visionppi.objects

/**
 * Created by Apoorva M K on 26/06/19.
 */

data class User constructor(var username: String,
                            var userId: Int,
                            var base64EncodedAuthenticationKey :String,
                            var authenticated : Boolean,
                            var officeId : Int,
                            var officeName : String,
                            var roles : List<Role>,
                            var permissions : List<String>,
                            var staffId : Int,
                            var staffDisplayName: String)



