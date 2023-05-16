package com.robert.passwordmanager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.ui.AccountViewModel
import com.robert.passwordmanager.adapters.AccountsAdapter
import com.robert.passwordmanager.R
import com.robert.passwordmanager.databinding.FragmentHomeBinding
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.ui.MainActivity


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var accountsAdapter: AccountsAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel = (activity as MainActivity).accountViewModel

        val categories = resources.getStringArray(R.array.categories)
        setupRecyclerView()

        accountViewModel.averagePasswordStrength.observe(viewLifecycleOwner){ averageStrength->
            binding.image.setHealthScore(averageStrength)
        }

        accountViewModel.report.observe(viewLifecycleOwner){ report->
            binding.totalText.text = report.total.toString()
            binding.weakText.text = report.weak.toString()
            binding.reusedText.text  = report.reused.toString()
            binding.strongText.text = report.strong.toString()
        }


        accountViewModel.allAccounts.observe(viewLifecycleOwner) { list ->
            list.let {
                accountsAdapter.submitList(it)
                //updateTextViews(accountViewModel.getSizeOfEachCategory(categories, it))
            }
        }
    }

    private fun setupRecyclerView() {
        accountsAdapter = AccountsAdapter()
        val myLayoutManager = LinearLayoutManager(context)
        myLayoutManager.orientation = RecyclerView.VERTICAL
        binding.recentRv.apply {
            adapter = accountsAdapter
            layoutManager = myLayoutManager
        }
    }

    private fun deletePassword(passwordDetails: Account) {
        accountViewModel.deleteAccount(passwordDetails)
    }
}