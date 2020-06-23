package org.mifos.visionppi.api.services

import io.reactivex.Observable
import org.mifos.visionppi.api.ApiEndPoints
import org.mifos.visionppi.models.User
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author yashk2000
 * @since 22/06/2020
 */
interface AuthenticationService {
    @POST(ApiEndPoints.AUTHENTICATION)
    fun authenticate(@Query("username") username: String?,
                     @Query("password") password: String?): Observable<User?>?
}