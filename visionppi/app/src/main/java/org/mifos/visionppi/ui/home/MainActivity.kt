package org.mifos.visionppi.ui.home

import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.mifos.visionppi.R
import org.mifos.visionppi.adapters.ClientSearchAdapter
import org.mifos.visionppi.databinding.ActivityMainBinding
import org.mifos.visionppi.objects.Client
import org.mifos.visionppi.ui.client_profile.ClientProfileActivity
import org.mifos.visionppi.ui.user_profile.UserProfileActivity


/**
 * Created by Apoorva M K on 25/06/19.
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainMVPView {

    lateinit var binding: ActivityMainBinding
    lateinit var clientList : List<Client>
    var mMainPresenter: MainPresenter = MainPresenter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        client_search_list.layoutManager = LinearLayoutManager(this)


        search_btn.setOnClickListener {

            search_query.onEditorAction(EditorInfo.IME_ACTION_DONE)
            if(search_query.text.toString().length == 0)
                searchError()

            else
                search(search_query.text.toString())
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(applicationContext, UserProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_home -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun searchError() {
        showToastMessage(getString(R.string.empty_query))
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    override fun searchUnsuccessful() {
        showToastMessage(getString(R.string.search_unsuccessful))
    }

    override fun search(string: String) {

        clientList = mMainPresenter.searchClients(string, applicationContext, this)

        if(clientList.size==0)
            searchUnsuccessful()

        client_search_list.adapter = ClientSearchAdapter(clientList, this, { item -> onClick(item)})
    }

    private fun onClick(item: Client) {

        val intent = Intent(applicationContext, ClientProfileActivity::class.java)
        intent.putExtra("client",item)
        startActivity(intent)

    }
}
