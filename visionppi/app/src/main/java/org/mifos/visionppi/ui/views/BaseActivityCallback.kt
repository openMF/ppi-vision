package org.mifos.visionppi.ui.views

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
interface BaseActivityCallback {
    fun showProgressDialog(message: String?)
    fun hideProgressDialog()
    fun setToolbarTitle(title: String?)
}
