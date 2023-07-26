package org.mifos.visionppi.ui.menu
import org.mifos.visionppi.base.MVPView

interface MenuMVPView : MVPView {
    fun getUserDetails()

    fun setUserDetails()
}
