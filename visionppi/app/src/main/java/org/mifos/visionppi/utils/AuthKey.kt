package org.mifos.visionppi.utils

import khttp.structures.authorization.Authorization

data class AuthKey(val authKey: String) : Authorization {
    override val header: Pair<String, String>
        get() {
            return "Authorization" to "Basic $authKey"
        }
}
