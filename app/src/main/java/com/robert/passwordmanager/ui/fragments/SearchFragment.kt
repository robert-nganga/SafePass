package com.robert.passwordmanager.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.ui.PasswordViewModel
import com.robert.passwordmanager.R
import com.robert.passwordmanager.adapters.PasswordsAdapter
import com.robert.passwordmanager.databinding.FragmentSearchBinding
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.ui.MainActivity

class SearchFragment : Fragment() {

    private lateinit var txtSearch: SearchView
    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var searchAdapter: PasswordsAdapter

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
        passwordViewModel = (activity as MainActivity).passwordViewModel

        txtSearch = view.findViewById(R.id.searchView)
        txtSearch.clearFocus()

        setupSearchRecyclerView()

        txtSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    passwordViewModel.setSearchQuery(it)
                }
                //searchPasswords(newText, recentsAdapter)
                return true
            }
        })

        passwordViewModel.searchResults.observe(viewLifecycleOwner){results->
            searchAdapter.updateList(results)
        }
    }

    private fun setupSearchRecyclerView() {
        searchAdapter = PasswordsAdapter(requireContext()){deletePassword(it)}
        val searchLayoutManager = LinearLayoutManager(context)
        searchLayoutManager.orientation = RecyclerView.VERTICAL

        binding.searchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = searchLayoutManager
        }
    }

    private fun deletePassword(passwordDetails: Account) {
        passwordViewModel.deleteAccount(passwordDetails)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}