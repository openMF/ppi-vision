package org.mifos.visionppi.ui.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ActivityLoginBinding
import android.os.StrictMode
import org.mifos.visionppi.ui.home.MainActivity


/**
 * Created by Apoorva M K on 25/06/19.
 */

class LoginActivity: AppCompatActivity(), LoginMVPView {

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var binding: ActivityLoginBinding
    private var mLoginPresenter: LoginPresenter  = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginBtn.setOnClickListener {
            if (networkAvailable(this)) {
                login()
            } else {
                showToastMessage("Internet connection not available. Please check network settings")
            }
        }

    }

    private fun login() {

        username = binding.etUsername.text.toString()
        password = binding.etPassword.text.toString()

        if (validateUserInput()) {

            val loginResult: Boolean = mLoginPresenter.login(username, password, applicationContext, this)

            if(loginResult){
                onLoginSuccessful()
            }
            else{
                onLoginError()
            }
        }
    }

    private fun validateUserInput(): Boolean {

        if (username.length < 5) {
            showToastMessage(getString(R.string.invalid_username))
            return false
        }
        if (password.length < 6) {
            showToastMessage(getString(R.string.invalid_passlen))
            return false
        }
        return true
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginSuccessful() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginError() {
        showToastMessage(getString(R.string.login_fail))
    }

    private fun networkAvailable (activity:AppCompatActivity): Boolean{
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return  networkInfo != null && networkInfo.isConnected
    }

}