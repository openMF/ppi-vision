package org.mifos.visionppi.models

import java.util.ArrayList
import org.mifos.visionppi.objects.Role

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */

data class User(
    var firstName: String,
    var middleName: String,
    var lastName: String,
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
