package org.mifos.visionppi.ui.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ActivityUserProfileBinding
import org.mifos.visionppi.databinding.ToolbarBinding
import org.mifos.visionppi.models.User

/**
 * Created by Apoorva M K on 27/06/19.
 */

class UserProfileActivity : Fragment(), UserProfileMVPView {

    lateinit var user: User
    private lateinit var activityUserProfileBinding: ActivityUserProfileBinding
    var mUserProfilePresenter: UserProfilePresenter = UserProfilePresenter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityUserProfileBinding=ActivityUserProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.activity_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserDetails()
    }

    override fun getUserDetails() {
        user = mUserProfilePresenter.fetchUserDetails(requireActivity(), requireContext())
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
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }
}
