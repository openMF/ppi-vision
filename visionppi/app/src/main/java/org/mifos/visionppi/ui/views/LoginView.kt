package org.mifos.visionppi.ui.views

import org.mifos.visionppi.ui.views.base.MVPView

/**
 * @author yashk2000
 * @since 22/06/2020
 */
interface LoginView : MVPView {
    /**
     * Should be called when the user credentials are successfully
     * authenticated from the API.
     * The username would be passed to the view so that we can
     * at least say hello!
     *
     * @param userName Username of the user that successfully logged in!
     */
    fun onLoginSuccess(userName: String?)

    /**
     * Should be called when there is a problem with the user input that we
     * requested or with trying to authenticate the user from the API.
     *
     * The error could be of many types like
     *
     *  * Invalid Login Credentials
     *  * No network connection
     *
     *
     * The problem must be communicated back to the user clearly.
     * @param errorMessage Error message that tells the user about the problem.
     */
    fun showMessage(errorMessage: String?)
    fun showUsernameError(error: String?)
    fun showPasswordError(error: String?)
    fun clearUsernameError()
    fun clearPasswordError()
}
