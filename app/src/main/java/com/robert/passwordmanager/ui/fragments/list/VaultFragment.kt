package com.robert.passwordmanager.ui.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.ui.PasswordViewModel
import com.robert.passwordmanager.R
import com.robert.passwordmanager.adapters.AllPasswordsAdapter
import com.robert.passwordmanager.models.PasswordDetails
import com.robert.passwordmanager.ui.MainActivity


class VaultFragment : Fragment() {

    private lateinit var categories: Array<String>
    private lateinit var passwordViewModel: PasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vault, container, false)
        categories = resources.getStringArray(R.array.categories)
        passwordViewModel = (activity as MainActivity).passwordViewModel

        val allPasswordsAdapter = AllPasswordsAdapter()
        val mainRecyclerView = view?.findViewById<RecyclerView>(R.id.mainRecyclerView)
        mainRecyclerView?.adapter = allPasswordsAdapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mainRecyclerView?.layoutManager = layoutManager

        passwordViewModel.allPasswords.observe(viewLifecycleOwner){
            allPasswordsAdapter.submitList(it)
        }
        return view
    }

    private fun deletePassword(passwordDetails: PasswordDetails) {
        passwordViewModel.delete(passwordDetails)
    }

}