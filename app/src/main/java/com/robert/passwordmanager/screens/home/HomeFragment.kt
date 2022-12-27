package com.robert.passwordmanager.screens.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.PasswordViewModel
import com.robert.passwordmanager.PasswordsAdapter
import com.robert.passwordmanager.R
import com.robert.passwordmanager.models.PasswordDetails


class HomeFragment : Fragment() {

    private val passwordViewModel: PasswordViewModel by viewModels {PasswordViewModel.Factory}
    private lateinit var txtWebsites: TextView
    private lateinit var txtApps: TextView
    private lateinit var txtCloud: TextView
    private lateinit var txtPayment: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val categories = resources.getStringArray(R.array.categories)
        txtWebsites = view.findViewById(R.id.txtWebsite)
        txtApps = view.findViewById(R.id.txtApplications)
        txtCloud = view.findViewById(R.id.txtCloud)
        txtPayment = view.findViewById(R.id.txtPayment)

        val passwordsAdapter = PasswordsAdapter(requireContext()) { deletePassword(it) }
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recentRv)
        recyclerView?.adapter = passwordsAdapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView?.layoutManager = layoutManager


        passwordViewModel.allPasswords.observe(viewLifecycleOwner) { list ->
            list.let {
                passwordsAdapter.updateList(it)
                updateTextViews(passwordViewModel.getSizeOfEachCategory(categories, it))
            }
        }
        return view
    }

    private fun deletePassword(passwordDetails: PasswordDetails) {
        passwordViewModel.delete(passwordDetails)
    }


    @SuppressLint("SetTextI18n")
    private fun updateTextViews(sizeMap: Map<String, Int>) {
        txtWebsites.text = sizeMap["Website"].toString() + " websites"
        txtApps.text = sizeMap["Application"].toString() + " apps"
        txtPayment.text = sizeMap["Payment"].toString() + " wallets"
        txtCloud.text = sizeMap["Cloud"].toString() + " passwords"
    }




}