package com.robert.passwordmanager.screens.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.PasswordViewModel
import com.robert.passwordmanager.R
import com.robert.passwordmanager.models.PasswordDetails


class ListFragment : Fragment() {

    private lateinit var categories: Array<String>
    private val passwordViewModel: PasswordViewModel by viewModels { PasswordViewModel.Factory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        categories = resources.getStringArray(R.array.categories)

        val allPasswordsAdapter = AllPasswordsAdapter(requireContext()){deletePassword(it)}
        val mainRecyclerView = view?.findViewById<RecyclerView>(R.id.mainRecyclerView)
        mainRecyclerView?.adapter = allPasswordsAdapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mainRecyclerView?.layoutManager = layoutManager

        passwordViewModel.allPasswords.observe(viewLifecycleOwner){
            val passwordList = passwordViewModel.getPasswordsByCategory(categories, it)
            allPasswordsAdapter.addSections(passwordList)

        }
        return view
    }

    private fun deletePassword(passwordDetails: PasswordDetails) {
        passwordViewModel.delete(passwordDetails)
    }

}