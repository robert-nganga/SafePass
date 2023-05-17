package com.robert.passwordmanager.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.robert.passwordmanager.ui.AccountViewModel
import com.robert.passwordmanager.R
import com.robert.passwordmanager.adapters.AccountItemsAdapter
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.models.AccountListItem
import com.robert.passwordmanager.ui.MainActivity
import com.robert.passwordmanager.utils.OrderBy


class VaultFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private lateinit var categories: Array<String>
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var accountItemsAdapter: AccountItemsAdapter
    private lateinit var toolbar: MaterialToolbar
    private lateinit var orderByItem: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vault, container, false)
        categories = resources.getStringArray(R.array.categories)
        accountViewModel = (activity as MainActivity).accountViewModel
        toolbar = view.findViewById(R.id.topAppBar)
        orderByItem = view.findViewById(R.id.action_orderBy)
        setupMenuClickListener()
        recyclerView = view.findViewById(R.id.mainRecyclerView)
        setupRecyclerView()

        accountItemsAdapter.itemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_listFragment_to_addAccountFragment, bundle)
        }


        accountItemsAdapter.setCopyClickListener {
            copyPasswordToClipboard(it)
        }

        accountViewModel.allAccountItems.observe(viewLifecycleOwner){
            accountItemsAdapter.submitList(it)
        }
        return view
    }

    private fun setupRecyclerView() {
        accountItemsAdapter = AccountItemsAdapter()

        recyclerView.apply {
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
        toolbar.setOnMenuItemClickListener { menu->
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

    private fun copyPasswordToClipboard(pass: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("simple text", pass)
        clipboard.setPrimaryClip(clip)
        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
            Toast.makeText(requireContext(), "Copied Password", Toast.LENGTH_SHORT).show()
    }

    private fun deletePassword(passwordDetails: Account) {
        accountViewModel.deleteAccount(passwordDetails)
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

}