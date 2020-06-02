package org.mifos.visionppi.ui.client_profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.toolbar.*
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ActivityClientProfileBinding
import org.mifos.visionppi.objects.Client
import org.mifos.visionppi.ui.new_survey.NewPPISurveyActivity

/**
 * Created by Apoorva M K 01/07/2019
 */

class ClientProfileActivity : AppCompatActivity() , ClientProfileMVPView {

    lateinit var binding: ActivityClientProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_profile)
        val clientDetails = intent.getParcelableExtra("client") as Client
        setClientDetails(clientDetails)

        binding.newSurveyBtn.setOnClickListener {
            val intent= Intent(applicationContext, NewPPISurveyActivity::class.java)
            startActivity(intent)
        }

        setSupportActionBar(appToolbar)
        val actionBar = supportActionBar
        actionBar?.title = "Client Profile"

    }
    override fun setClientDetails(client : Client) {
        binding.clientIdValue.text = client.entityId.toString()
        binding.clientNameValue.text = client.entityName
        binding.accountNoValue.text = client.entityAccountNo
        binding.mobileNoValue.text = client.entityMobileNo
        binding.clientTypeValue.text = client.entityType
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

}