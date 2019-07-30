package org.mifos.visionppi.ui.login

import org.mifos.visionppi.base.MVPView

/**
 * Created by Apoorva M K on 25/06/19.
 */

interface LoginMVPView : MVPView {

    //fun showToastMessage(string: String)

    fun onLoginSuccessful()

    fun onLoginError()

}