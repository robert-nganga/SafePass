package com.robert.passwordmanager.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.ui.AccountViewModel
import com.robert.passwordmanager.R
import com.robert.passwordmanager.adapters.AccountsAdapter
import com.robert.passwordmanager.databinding.FragmentSearchBinding
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.ui.MainActivity

class SearchFragment : Fragment() {

    private lateinit var txtSearch: SearchView
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var searchAdapter: AccountsAdapter

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel = (activity as MainActivity).accountViewModel

        txtSearch = view.findViewById(R.id.searchView)
        txtSearch.clearFocus()

        setupSearchRecyclerView()

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    accountViewModel.setSearchQuery(it)
                }
                //searchPasswords(newText, recentsAdapter)
                return true
            }
        })

        accountViewModel.searchResults.observe(viewLifecycleOwner){ results->
            Log.i("SearchFragment", results.toString())
            searchAdapter.submitList(results)
        }
    }

    private fun setupSearchRecyclerView() {
        searchAdapter = AccountsAdapter()
        val searchLayoutManager = LinearLayoutManager(context)
        searchLayoutManager.orientation = RecyclerView.VERTICAL

        binding.searchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = searchLayoutManager
        }
    }

    private fun deletePassword(passwordDetails: Account) {
        accountViewModel.deleteAccount(passwordDetails)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}