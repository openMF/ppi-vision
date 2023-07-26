package org.mifos.visionppi.ui.menu
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import org.mifos.visionppi.R
import org.mifos.visionppi.api.local.PreferencesHelper
import org.mifos.visionppi.databinding.ActivityMenuBinding
import org.mifos.visionppi.models.User
import org.mifos.visionppi.ui.AboutActivity
import org.mifos.visionppi.ui.activities.LoginActivity
import org.mifos.visionppi.ui.user_profile.UserProfileActivity
import org.mifos.visionppi.utils.PrefManager

class MenuActivity : Fragment(), MenuMVPView {

    lateinit var user: User
    private lateinit var activityMenuBinding: ActivityMenuBinding
    var mMenuPresenter: MenuPresenter = MenuPresenter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityMenuBinding=ActivityMenuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val shareButton = activityMenuBinding.shareButton
        val editProfileButton = activityMenuBinding.editProfileButton
        val aboutButton = activityMenuBinding.aboutButton
        val logoutButton = activityMenuBinding.logoutButton

        shareButton.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.install_vision).toString() + "\n" + getString(R.string.download_link)
            )
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        }

        editProfileButton.setOnClickListener {
            val editIntent = Intent(requireContext(), UserProfileActivity::class.java)
            startActivity(editIntent)
        }

        aboutButton.setOnClickListener{
            val aboutIntent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(aboutIntent)
        }

        logoutButton.setOnClickListener{
            showLogoutDialog()
        }

        return activityMenuBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserDetails()
    }

    override fun getUserDetails() {
        user = mMenuPresenter.fetchUserDetails(requireActivity(), requireContext())
        setUserDetails()
    }

    override fun setUserDetails() {
        activityMenuBinding.uname.text = user.username
        activityMenuBinding.usernameValue.text = user.username
        activityMenuBinding.userIdValue.text = user.userId.toString()
        activityMenuBinding.officeNameValue.text = user.officeName
        activityMenuBinding.officeIdValue.text = user.officeId.toString()
        activityMenuBinding.staffIdValue.text = user.staffId.toString()
        activityMenuBinding.staffDispNameValue.text = user.staffDisplayName
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    fun showLogoutDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setMessage("Are you sure you want to logout?")
        dialogBuilder.setPositiveButton("Yes") { _, _ ->
            val preferencesHelper = PreferencesHelper(requireContext())
            preferencesHelper.clear()
            val prefManager = PrefManager()
            prefManager.clear(requireActivity(), requireContext())
            val intent = Intent(requireContext().applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        dialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}
