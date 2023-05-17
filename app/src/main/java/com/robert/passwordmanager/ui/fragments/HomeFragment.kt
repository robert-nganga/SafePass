package com.robert.passwordmanager.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robert.passwordmanager.ui.AccountViewModel
import com.robert.passwordmanager.adapters.AccountsAdapter
import com.robert.passwordmanager.R
import com.robert.passwordmanager.databinding.FragmentHomeBinding
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.models.AccountListItem
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

        accountsAdapter.setCopyClickListener { password->
            copyPasswordToClipboard(password)
        }

        accountsAdapter.itemClickListener { account ->
            val bundle = Bundle().apply {
                putInt("id", account.id)
            }

            findNavController().navigate(R.id.action_homeFragment_to_addAccountFragment, bundle)
        }


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

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val account = accountsAdapter.currentList[viewHolder.adapterPosition]
            accountViewModel.deleteAccount(account)
            Snackbar.make(requireView(), "Account deleted", Snackbar.LENGTH_LONG)
                .setAnchorView((activity as MainActivity).findViewById(R.id.bottomAppBar))
                .setAction("Undo"){
                    accountViewModel.insertAccount(account)
                }
                .show()

        }
    })

    private fun copyPasswordToClipboard(pass: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("simple text", pass)
        clipboard.setPrimaryClip(clip)
        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
            Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
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