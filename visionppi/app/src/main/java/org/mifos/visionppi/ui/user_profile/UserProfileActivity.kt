package org.mifos.visionppi.ui.user_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.mifos.visionppi.R
import org.mifos.visionppi.models.User

/**
 * Created by Apoorva M K on 27/06/19.
 */

class UserProfileActivity : Fragment(), UserProfileMVPView{

    lateinit var user: User
    var mUserProfilePresenter : UserProfilePresenter = UserProfilePresenter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.activity_user_profile, container, false)
        return root
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

        uname.text = user.username
        username_value.text = user.username
        user_id_value.text = user.userId.toString()
        office_name_value.text = user.officeName
        office_id_value.text = user.officeId.toString()
        staff_id_value.text = user.staffId.toString()
        staff_disp_name_value.text = user.staffDisplayName

    }

    override fun showToastMessage(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }
}