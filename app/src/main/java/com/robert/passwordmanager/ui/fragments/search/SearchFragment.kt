package com.robert.passwordmanager.ui.fragments.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.ui.PasswordViewModel
import com.robert.passwordmanager.R
import com.robert.passwordmanager.adapters.PasswordsAdapter
import com.robert.passwordmanager.models.PasswordDetails
import com.robert.passwordmanager.ui.MainActivity
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var txtSearch: SearchView
    private lateinit var passwordViewModel: PasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        passwordViewModel = (activity as MainActivity).passwordViewModel

        txtSearch = view.findViewById(R.id.searchView)
        txtSearch.clearFocus()

        val recentsAdapter = PasswordsAdapter(requireContext()){deletePassword(it)}
        val recyclerView = view?.findViewById<RecyclerView>(R.id.searchRecyclerView)
        recyclerView?.adapter = recentsAdapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView?.layoutManager = layoutManager

        txtSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchPasswords(newText, recentsAdapter)
                return true
            }
        })

        return view
    }

    private fun deletePassword(passwordDetails: PasswordDetails) {
        passwordViewModel.delete(passwordDetails)
    }

    private fun searchPasswords(newText: String?, passwordsAdapter: PasswordsAdapter) {
        if (newText != null) {
            if (newText.length > 1){
                val searchText = "$newText%"
                viewLifecycleOwner.lifecycleScope.launch {
                    passwordViewModel.searchPasswords(searchText).collect{
                        passwordsAdapter.updateList(it)
                    }
                }
            }
        }

    }
}