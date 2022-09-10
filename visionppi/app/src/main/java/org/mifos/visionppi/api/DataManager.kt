package org.mifos.visionppi.api

import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton
import org.mifos.visionppi.api.local.PreferencesHelper
import org.mifos.visionppi.models.User

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
@Singleton
class DataManager @Inject constructor(val preferencesHelper: PreferencesHelper, private val baseApiManager: BaseApiManager) {
    fun login(username: String?, password: String?): Observable<User?>? {
        val paramsMap = mapOf(
            "username" to username!!,
            "password" to password!!
        )
        return baseApiManager.authenticationApi.authenticate(paramsMap)
    }
}
