package org.mifos.visionppi.ui.views

/**
 * @author yashk2000
 * @since 22/06/2020
 */
interface BaseActivityCallback {
    fun showProgressDialog(message: String?)
    fun hideProgressDialog()
    fun setToolbarTitle(title: String?)
}