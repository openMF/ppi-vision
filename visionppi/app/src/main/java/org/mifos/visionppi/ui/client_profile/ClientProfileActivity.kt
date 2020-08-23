package org.mifos.visionppi.ui.client_profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_client_profile.accountNoValue
import kotlinx.android.synthetic.main.activity_client_profile.clientIdValue
import kotlinx.android.synthetic.main.activity_client_profile.clientNameValue
import kotlinx.android.synthetic.main.activity_client_profile.clientTypeValue
import kotlinx.android.synthetic.main.activity_client_profile.mobileNoValue
import kotlinx.android.synthetic.main.activity_client_profile.new_survey_btn
import kotlinx.android.synthetic.main.activity_client_profile.uname
import kotlinx.android.synthetic.main.toolbar.appToolbar
import org.mifos.visionppi.R
import org.mifos.visionppi.objects.Client
import org.mifos.visionppi.ui.new_survey.NewPPISurveyActivity

/**
 * Created by Apoorva M K 01/07/2019
 */

class ClientProfileActivity : AppCompatActivity(), ClientProfileMVPView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_client_profile)
        val clientDetails = intent.getParcelableExtra("client") as Client
        setClientDetails(clientDetails)

        setSupportActionBar(appToolbar)
        val actionBar = supportActionBar
        actionBar?.title = "Client Profile"

        new_survey_btn.setOnClickListener {
            val intent = Intent(applicationContext, NewPPISurveyActivity::class.java)
            startActivity(intent)
        }
    }

    override fun setClientDetails(client: Client) {
        uname.text = client.entityName
        clientIdValue.text = client.entityId.toString()
        clientNameValue.text = client.entityName
        accountNoValue.text = client.entityAccountNo
        mobileNoValue.text = client.entityMobileNo
        clientTypeValue.text = client.entityType
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
}
