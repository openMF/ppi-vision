package org.mifos.visionppi.ui.user_profile

import org.mifos.visionppi.base.MVPView

/**
 * Created by Apoorva M K on 27/06/19.
 */

interface UserProfileMVPView:MVPView {
    fun getUserDetails()

    fun setUserDetails()
}