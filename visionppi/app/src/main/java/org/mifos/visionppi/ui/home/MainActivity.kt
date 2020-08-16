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
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.mifos.visionppi.R
import org.mifos.visionppi.adapters.ClientSearchAdapter
import org.mifos.visionppi.objects.Client
import org.mifos.visionppi.ui.client_profile.ClientProfileActivity


/**
 * Created by Apoorva M K on 25/06/19.
 */

class MainActivity : Fragment(), MainMVPView {

    lateinit var clientList: List<Client>
    var mMainPresenter: MainPresenter = MainPresenter()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.content_main, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        client_search_list.layoutManager = LinearLayoutManager(requireContext())

        search_query.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })

        search_query.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isEmpty()) {
                    search_btn.isEnabled = false
                    search_btn.alpha = 0.5F
                } else {
                    search_btn.isEnabled = true
                    search_btn.alpha = 1.0F
                }
            }
        })

        search_btn.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        if (networkAvailable(requireActivity())) {
            search_query.onEditorAction(EditorInfo.IME_ACTION_DONE)
            if (search_query.text.toString().length == 0)
                searchError()
            else {
                val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(search_btn.windowToken, 0)
                search(search_query.text.toString())
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

            clientList = doSearch(string)

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

    fun doSearch(string: String): List<Client> {
        return mMainPresenter.searchClients(string, requireContext(), requireActivity())
    }

    fun makelist() {
        if (clientList.size == 0)
            searchUnsuccessful()

        client_search_list.adapter =
                ClientSearchAdapter(clientList, requireContext(), { item -> onClick(item) })
    }
}
