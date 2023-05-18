package com.robert.passwordmanager.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robert.passwordmanager.R
import com.robert.passwordmanager.adapters.AccountItemsAdapter
import com.robert.passwordmanager.databinding.FragmentVaultBinding
import com.robert.passwordmanager.models.AccountListItem
import com.robert.passwordmanager.ui.AccountViewModel
import com.robert.passwordmanager.ui.MainActivity
import com.robert.passwordmanager.utils.OrderBy
import com.robert.passwordmanager.utils.Utilities.copyPasswordToClipboard


class VaultFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private lateinit var categories: Array<String>
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var accountItemsAdapter: AccountItemsAdapter
    private lateinit var orderByItem: View

    private var _binding: FragmentVaultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categories = resources.getStringArray(R.array.categories)
        accountViewModel = (activity as MainActivity).accountViewModel
        orderByItem = view.findViewById(R.id.action_orderBy)
        setupMenuClickListener()
        setupRecyclerView()

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        accountItemsAdapter.itemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_listFragment_to_addAccountFragment, bundle)
        }

        accountItemsAdapter.setCopyClickListener {
            copyPasswordToClipboard(requireContext(), it)
        }

        accountViewModel.allAccountItems.observe(viewLifecycleOwner){
            accountItemsAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        accountItemsAdapter = AccountItemsAdapter()

        binding.mainRecyclerView.apply {
            adapter = accountItemsAdapter
            itemTouchHelper.attachToRecyclerView(this)
            itemAnimator = null
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
            val accountListItem = accountItemsAdapter.currentList[viewHolder.adapterPosition]
            when(accountListItem){
                is AccountListItem.AccountItem ->{
                    accountViewModel.deleteAccount(accountListItem.account)
                    Snackbar.make(requireView(), "Deleted", Snackbar.LENGTH_LONG)
                        .setAnchorView((activity as MainActivity).findViewById(R.id.bottomAppBar))
                        .setAction("Undo"){
                            accountViewModel.insertAccount(accountListItem.account)
                        }
                        .show()
                }
                is AccountListItem.AccountHeaderItem -> {}
            }

        }
    })

    private fun setupMenuClickListener() {
        binding.topAppBar.setOnMenuItemClickListener { menu->
            when(menu.itemId){
                R.id.action_deleteAll -> {
                    accountViewModel.deleteAll()
                    true
                }
                R.id.action_orderBy -> {
                    showPopup(orderByItem)
                    true
                }
                else -> false
            }
        }
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(context, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.order_items_menu, popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()
    }

    override fun onMenuItemClick(menu: MenuItem?): Boolean {
        return when (menu?.itemId){
            R.id.action_orderByDate ->{
                accountViewModel.setOrderBY(OrderBy.Date)
                true
            }
            R.id.action_orderByCategory ->{
                accountViewModel.setOrderBY(OrderBy.Category)
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}