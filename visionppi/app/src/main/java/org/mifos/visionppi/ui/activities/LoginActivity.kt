package org.mifos.visionppi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import javax.inject.Inject
//import kotlinx.android.synthetic.main.activity_login.til_password
//import kotlinx.android.synthetic.main.activity_login.til_username
import org.mifos.visionppi.MainActivity
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ActivityLoginBinding
import org.mifos.visionppi.databinding.ToolbarBinding
import org.mifos.visionppi.presenters.LoginPresenter
import org.mifos.visionppi.ui.activities.base.BaseActivity
import org.mifos.visionppi.ui.views.LoginView
import org.mifos.visionppi.utils.Network
import org.mifos.visionppi.utils.Toaster

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */
class LoginActivity : BaseActivity(), LoginView {
    @JvmField
    @Inject
    var loginPresenter: LoginPresenter? = null

    @JvmField
    @BindView(R.id.ll_login)
    var llLogin: LinearLayout? = null

    private var userName: String? = null
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        activityComponent!!.inject(this)
        val view = binding.root
        setContentView(view)
        ButterKnife.bind(this)
        loginPresenter!!.attachView(this)
    }

    /**
     * Called when Login is user has successfully logged in
     *
     * @param userName Username of the user that successfully logged in!
     */
    override fun onLoginSuccess(userName: String?) {
        this.userName = userName
        val i = Intent(this@LoginActivity,
                MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        finish()
    }

    /**
     * Shows ProgressDialog when called
     */
    override fun showProgress() {
        showProgressDialog(getString(R.string.progress_message_login))
    }

    /**
     * Hides the progressDialog which is being shown
     */
    override fun hideProgress() {
        hideProgressDialog()
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param errorMessage Error message that tells the user about the problem.
     */
    override fun showMessage(errorMessage: String?) {
        showToast(errorMessage!!, Toast.LENGTH_LONG)
        llLogin?.visibility = View.VISIBLE
    }

    override fun showUsernameError(error: String?) {
        binding.tilUsername.error = error
    }

    override fun showPasswordError(error: String?) {
        binding.tilPassword.error = error
    }

    override fun clearUsernameError() {
        binding.tilUsername.isErrorEnabled = false
    }

    override fun clearPasswordError() {
        binding.tilPassword.isErrorEnabled = false
    }

    /**
     * Called when Login Button is clicked, used for logging in the user
     */
    fun onLoginClicked(view: View) {
        val username = binding.tilUsername.editText?.editableText.toString()
        val password = binding.tilPassword.editText?.editableText.toString()
        if (Network.isConnected(this)) {
            loginPresenter?.login(username, password)
        } else {
            Toaster.show(llLogin!!, getString(R.string.no_internet_connection))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter?.detachView()
    }
}
