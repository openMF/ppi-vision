package org.mifos.visionppi.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.mifos.visionppi.R
import org.mifos.visionppi.adapters.ClientSearchAdapter
import org.mifos.visionppi.databinding.ActivityComputerVisionBinding
import org.mifos.visionppi.databinding.ContentMainBinding
import org.mifos.visionppi.objects.Client
import org.mifos.visionppi.ui.client_profile.ClientProfileActivity

/**
 * Created by Apoorva M K on 25/06/19.
 */

class MainActivity : Fragment(), MainMVPView {
    private lateinit var contentMainBinding: ContentMainBinding
    lateinit var clientList: List<Client>
    var mMainPresenter: MainPresenter = MainPresenter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.content_main, container, false)
        contentMainBinding=ContentMainBinding.inflate(layoutInflater)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentMainBinding.clientSearchList.layoutManager = LinearLayoutManager(requireContext())

        contentMainBinding.searchQuery.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })

        contentMainBinding.searchQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isEmpty()) {
                    contentMainBinding.searchBtn.isEnabled = false
                    contentMainBinding.searchBtn.alpha = 0.5F
                } else {
                    contentMainBinding.searchBtn.isEnabled = true
                    contentMainBinding.searchBtn.alpha = 1.0F
                }
            }
        })

        contentMainBinding.searchBtn.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        if (networkAvailable(requireActivity())) {
            contentMainBinding.searchQuery.onEditorAction(EditorInfo.IME_ACTION_DONE)
            println("Going in")
            if (contentMainBinding.searchQuery.text.toString().isEmpty())
                searchError()
            else {
                println("going in deeper")
                val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(contentMainBinding.searchBtn.windowToken, 0)
                search(contentMainBinding.searchQuery.text.toString())
            }
        } else {
            showToastMessage("Internet connection not available. Please check network settings")
        }
    }

    private fun networkAvailable(activity: FragmentActivity): Boolean {
        val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun searchError() {
        showToastMessage(getString(R.string.empty_query))
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    override fun searchUnsuccessful() {
        showToastMessage(getString(R.string.search_unsuccessful))
    }

    override fun search(string: String) {
        val future = doAsync {
            println("fetching client list")

            clientList = doSearch(string)

            println("fetched client list $clientList")

            uiThread {
                makelist()
            }
        }
    }

    private fun onClick(item: Client) {

        val intent = Intent(context, ClientProfileActivity::class.java)
        intent.putExtra("client", item)
        startActivity(intent)
    }

    private fun doSearch(string: String): List<Client> {
        return mMainPresenter.searchClients(string, requireContext(), requireActivity())
    }

    private fun makelist() {
        if (clientList.isEmpty())
            searchUnsuccessful()

        contentMainBinding.clientSearchList.adapter =
                ClientSearchAdapter(clientList, requireContext()) { item -> onClick(item) }
    }
}
