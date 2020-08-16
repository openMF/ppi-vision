package org.mifos.visionppi.ui.activities

import android.content.Intent
import android.os.Bundle
import org.mifos.visionppi.api.local.PreferencesHelper
import org.mifos.visionppi.ui.activities.base.BaseActivity
import org.mifos.visionppi.MainActivity

/**
 * @author yashk2000
 * @since 22/06/2020
 */
class SplashActivity : BaseActivity() {
    var i: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent!!.inject(this)
        val preferencesHelper = PreferencesHelper(applicationContext)
        if (preferencesHelper.isAuthenticated) {
            i = Intent(this@SplashActivity,
                    MainActivity::class.java)
            i?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        } else {
            i = Intent(this, LoginActivity::class.java)
        }
        startActivity(i)
        finish()
    }

    override fun showProgressDialog(message: String?) {
        TODO("Not yet implemented")
    }

    override fun setToolbarTitle(title: String?) {
        TODO("Not yet implemented")
    }
}