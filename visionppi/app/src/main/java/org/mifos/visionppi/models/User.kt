package org.mifos.visionppi.models

import java.util.ArrayList
import org.mifos.visionppi.objects.Role

/**
 * @author yashk2000
 * @since 22/06/2020
 */

data class User(

    var userId: Long = 0,
    var isAuthenticated: Boolean = false,
    var username: String? = null,
    var base64EncodedAuthenticationKey: String? = null,
    var permissions: List<String> = ArrayList(),
    var officeId: Int,
    var officeName: String,
    var roles: List<Role>,
    var staffId: Int,
    var staffDisplayName: String
)
