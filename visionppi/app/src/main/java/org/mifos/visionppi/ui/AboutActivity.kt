package org.mifos.visionppi.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*
import org.mifos.visionppi.MainActivity
import org.mifos.visionppi.R

class AboutActivity : AppCompatActivity() {
    var contributors = "https://github.com/openMF/ppi-vision/graphs/contributors"
    var gitHub = "https://github.com/openMF/ppi-vision"
    var twitter = "https://twitter.com/mifos"
    var license = "https://github.com/openMF/ppi-vision/blob/master/LICENSE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(appToolbar)
        val actionBar = supportActionBar
        actionBar?.title = "About"

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goToWeb(view: View?) {
        link(contributors)
    }

    fun goToGit(view: View?) {
        link(gitHub)
    }

    fun goToTwitter(view: View?) {
        link(twitter)
    }

    fun goToLicense(view: View?) {
        link(license)
    }

    private fun link(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
