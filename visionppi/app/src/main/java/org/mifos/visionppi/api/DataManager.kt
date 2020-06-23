package org.mifos.visionppi.api

import io.reactivex.Observable
import org.mifos.visionppi.api.local.PreferencesHelper
import org.mifos.visionppi.models.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author yashk2000
 * @since 22/06/2020
 */
@Singleton
class DataManager @Inject constructor(val preferencesHelper: PreferencesHelper, private val baseApiManager: BaseApiManager) {
    fun login(username: String?, password: String?): Observable<User?>? {
        return baseApiManager.authenticationApi.authenticate(username, password)
    }

}