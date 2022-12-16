package com.robert.passwordmanager.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.PasswordViewModel
import com.robert.passwordmanager.PasswordsAdapter
import com.robert.passwordmanager.R


class HomeFragment : Fragment() {

    private val passwordViewModel: PasswordViewModel by viewModels {PasswordViewModel.Factory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val passwordsAdapter = PasswordsAdapter(requireContext())
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recentRv)
        recyclerView?.adapter = passwordsAdapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView?.layoutManager = layoutManager


        passwordViewModel.allPasswords.observe(viewLifecycleOwner) { list ->
            list.let {
                passwordsAdapter.updateList(it)
            }
        }
        return view
    }

}