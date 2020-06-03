package org.mifos.visionppi.ui.user_profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.toolbar.*
import org.mifos.visionppi.objects.User
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ActivityUserProfileBinding

/**
 * Created by Apoorva M K on 27/06/19.
 */

class UserProfileActivity : AppCompatActivity(), UserProfileMVPView{

    lateinit var user:User
    lateinit var binding: ActivityUserProfileBinding
    var mUserProfilePresenter : UserProfilePresenter = UserProfilePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)

        setSupportActionBar(appToolbar)
        val actionBar = supportActionBar
        actionBar?.title = "User Profile"

        getUserDetails()
    }

    override fun getUserDetails() {
        user = mUserProfilePresenter.fetchUserDetails(this, applicationContext)
        setUserDetails()
    }

    override fun setUserDetails() {

        binding.usernameValue.setText(user.username)
        binding.userIdValue.setText(user.userId.toString())
        binding.officeNameValue.setText(user.officeName)
        binding.officeIdValue.setText(user.officeId.toString())
        binding.staffIdValue.setText(user.staffId.toString())
        binding.staffDispNameValue.setText(user.staffDisplayName)

    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
}