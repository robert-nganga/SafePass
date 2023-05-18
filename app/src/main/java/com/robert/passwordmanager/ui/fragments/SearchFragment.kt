package com.robert.passwordmanager.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.R
import com.robert.passwordmanager.adapters.AccountsAdapter
import com.robert.passwordmanager.databinding.FragmentSearchBinding
import com.robert.passwordmanager.ui.AccountViewModel
import com.robert.passwordmanager.ui.MainActivity
import com.robert.passwordmanager.utils.Utilities

class SearchFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var searchAdapter: AccountsAdapter

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel = (activity as MainActivity).accountViewModel
        binding.searchView.clearFocus()
        setupSearchRecyclerView()

        searchAdapter.setCopyClickListener { password->
            Utilities.copyPasswordToClipboard(requireContext(), password)
        }

        searchAdapter.itemClickListener { account ->
            val bundle = Bundle().apply {
                putInt("id", account.id)
            }
            findNavController().navigate(R.id.action_searchFragment_to_addAccountFragment, bundle)
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}