package com.robert.passwordmanager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.ui.PasswordViewModel
import com.robert.passwordmanager.adapters.PasswordsAdapter
import com.robert.passwordmanager.R
import com.robert.passwordmanager.databinding.FragmentHomeBinding
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.ui.MainActivity


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var passwordsAdapter: PasswordsAdapter

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
        passwordViewModel = (activity as MainActivity).passwordViewModel

        val categories = resources.getStringArray(R.array.categories)
        setupRecyclerView()

        passwordViewModel.averagePasswordStrength.observe(viewLifecycleOwner){averageStrength->
            binding.image.setHealthScore(averageStrength)
        }

        passwordViewModel.report.observe(viewLifecycleOwner){report->
            binding.totalText.text = report.total.toString()
            binding.weakText.text = report.weak.toString()
            binding.reusedText.text  = report.reused.toString()
            binding.strongText.text = report.strong.toString()
        }


        passwordViewModel.allAccounts.observe(viewLifecycleOwner) { list ->
            list.let {
                passwordsAdapter.updateList(it)
                //updateTextViews(passwordViewModel.getSizeOfEachCategory(categories, it))
            }
        }
    }

    private fun setupRecyclerView() {
        passwordsAdapter = PasswordsAdapter(requireContext()) { deletePassword(it) }
        val myLayoutManager = LinearLayoutManager(context)
        myLayoutManager.orientation = RecyclerView.VERTICAL
        binding.recentRv.apply {
            adapter = passwordsAdapter
            layoutManager = myLayoutManager
        }
    }

    private fun deletePassword(passwordDetails: Account) {
        passwordViewModel.deleteAccount(passwordDetails)
    }
}