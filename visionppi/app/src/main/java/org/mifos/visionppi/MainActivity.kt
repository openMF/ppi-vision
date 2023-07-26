package org.mifos.visionppi

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.toolbar.*
import org.mifos.visionppi.api.local.PreferencesHelper
import org.mifos.visionppi.ui.AboutActivity
import org.mifos.visionppi.ui.activities.LoginActivity
import org.mifos.visionppi.utils.PrefManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(appToolbar)
        val actionBar = supportActionBar
        actionBar?.title = "OK"
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                showLogoutDialog()
                true
            }
            R.id.share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        getString(R.string.install_vision).toString() + "\n" + getString(R.string.download_link)
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
                true
            }
            R.id.about -> {
                intent.setClass(this, AboutActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showLogoutDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Are you sure you want to logout?")
        dialogBuilder.setPositiveButton("Yes") { _, _ ->
            val preferencesHelper = PreferencesHelper(baseContext)
            preferencesHelper.clear()
            val prefManager = PrefManager()
            prefManager.clear(this, baseContext)
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        dialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}
