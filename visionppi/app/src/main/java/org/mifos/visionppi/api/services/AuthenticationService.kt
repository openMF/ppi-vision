package org.mifos.visionppi.api.services

import io.reactivex.Observable
import org.mifos.visionppi.api.ApiEndPoints
import org.mifos.visionppi.models.User
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
interface AuthenticationService {
    @POST(ApiEndPoints.AUTHENTICATION)
    fun authenticate(
        @Body body: Map<String, String>
    ): Observable<User?>?
}
