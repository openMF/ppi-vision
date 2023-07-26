package org.mifos.visionppi.ui.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ActivityUserProfileBinding
import org.mifos.visionppi.databinding.ToolbarBinding
import org.mifos.visionppi.models.User

/**
 * Created by Apoorva M K on 27/06/19.
 */

class UserProfileActivity : AppCompatActivity(), UserProfileMVPView {

    lateinit var user: User
    private lateinit var activityUserProfileBinding: ActivityUserProfileBinding
    var mUserProfilePresenter: UserProfilePresenter = UserProfilePresenter()
    private lateinit var binding: ToolbarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ToolbarBinding.inflate(layoutInflater)
        activityUserProfileBinding=ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(activityUserProfileBinding.root)
        setSupportActionBar(binding.appToolbar)
        getUserDetails()
    }

    override fun getUserDetails() {
        user = mUserProfilePresenter.fetchUserDetails(this, this)
        setUserDetails()
    }

    override fun setUserDetails() {

        activityUserProfileBinding.uname.text = user.username
        activityUserProfileBinding.usernameValue.text = user.username
        activityUserProfileBinding.userIdValue.text = user.userId.toString()
        activityUserProfileBinding.officeNameValue.text = user.officeName
        activityUserProfileBinding.officeIdValue.text = user.officeId.toString()
        activityUserProfileBinding.staffIdValue.text = user.staffId.toString()
        activityUserProfileBinding.staffDispNameValue.text = user.staffDisplayName
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
}
