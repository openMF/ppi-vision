package org.mifos.visionppi.ui.client_profile

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.mifos.visionppi.MainActivity
import org.mifos.visionppi.databinding.ActivityClientProfileBinding
import org.mifos.visionppi.databinding.ToolbarBinding
import org.mifos.visionppi.objects.Client
import org.mifos.visionppi.ui.AnalyzeActvity


/**
 * Created by Apoorva M K 01/07/2019
 */

class ClientProfileActivity : AppCompatActivity(), ClientProfileMVPView {
    private lateinit var toolBarBinding: ToolbarBinding
    private lateinit var activityClientProfileBinding: ActivityClientProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityClientProfileBinding=ActivityClientProfileBinding.inflate(layoutInflater)
        setContentView(activityClientProfileBinding.root)

        toolBarBinding = ToolbarBinding.inflate(layoutInflater)

        val clientDetails = intent.getParcelableExtra("client") as Client?

        if (clientDetails != null) {
            setClientDetails(clientDetails)
        }

        setSupportActionBar(toolBarBinding.appToolbar)
        val actionBar = supportActionBar
        actionBar?.title = "Client Profile"

        activityClientProfileBinding.newSurveyBtn.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("clientDetails", clientDetails)
            startActivity(intent)
            finish()

        }

        activityClientProfileBinding.prevSurveyBtn.setOnClickListener {

            Toast.makeText(this, "Stay Tuned!\n We're Bringing This Feature Soon", Toast.LENGTH_SHORT).show()

        }

    }

    override fun setClientDetails(client: Client) {
        activityClientProfileBinding.uname.text = client?.entityName
        activityClientProfileBinding.clientIdValue.text = client?.entityId.toString()
        activityClientProfileBinding.clientNameValue.text = client?.entityName
        activityClientProfileBinding.accountNoValue.text = client?.entityAccountNo
        activityClientProfileBinding.mobileNoValue.text = client?.entityMobileNo
        activityClientProfileBinding.clientTypeValue.text = client?.entityType
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
}
