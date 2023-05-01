package com.robert.passwordmanager.ui.fragments.list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.robert.passwordmanager.ui.PasswordViewModel
import com.robert.passwordmanager.R
import com.robert.passwordmanager.adapters.AllPasswordsAdapter
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.ui.MainActivity


class VaultFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private lateinit var categories: Array<String>
    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var toolbar: MaterialToolbar
    private lateinit var orderByItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vault, container, false)
        categories = resources.getStringArray(R.array.categories)
        passwordViewModel = (activity as MainActivity).passwordViewModel
        toolbar = view.findViewById(R.id.topAppBar)
        orderByItem = view.findViewById(R.id.action_orderBy)
        toolbar.inflateMenu(R.menu.top_app_bar)
        setupMenuClickListener()


        val allPasswordsAdapter = AllPasswordsAdapter()
        val mainRecyclerView = view?.findViewById<RecyclerView>(R.id.mainRecyclerView)
        mainRecyclerView?.adapter = allPasswordsAdapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        mainRecyclerView?.layoutManager = layoutManager

        allPasswordsAdapter.setCopyClickListener {
            copyPasswordToClipboard(it)
        }

        passwordViewModel.allAccountItems.observe(viewLifecycleOwner){
            allPasswordsAdapter.submitList(it)
        }
        return view
    }

    private fun setupMenuClickListener() {
        toolbar.setOnMenuItemClickListener { menu->
            when(menu.itemId){
                R.id.action_deleteAll -> {
                    true
                }
                R.id.action_orderBy -> {

                    showPopup(toolbar)
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
        passwordViewModel.delete(passwordDetails)
    }

    override fun onMenuItemClick(menu: MenuItem?): Boolean {
        return when (menu?.itemId){
            R.id.action_orderByDate ->{
                true
            }
            R.id.action_orderByCategory ->{
                true
            }
            else -> false
        }
    }

}