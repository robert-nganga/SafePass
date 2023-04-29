package com.robert.passwordmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.R
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.models.AccountListItem
import com.robert.passwordmanager.utils.Contants.ITEM_VIEW_TYPE_HEADER
import com.robert.passwordmanager.utils.Contants.ITEM_VIEW_TYPE_ITEM
import java.lang.ClassCastException

class AllPasswordsAdapter(): ListAdapter<AccountListItem, RecyclerView.ViewHolder>(diffUtil) {

    private var btnCopyClickListener: ((String)->Unit)? = null

    fun setCopyClickListener(listener: (String)-> Unit){
        btnCopyClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val passwordItem = LayoutInflater.from(parent.context).inflate(R.layout.vault_item, parent, false)
        val headerItem = LayoutInflater.from(parent.context).inflate(R.layout.password_header, parent, false)
        return when (viewType){
            ITEM_VIEW_TYPE_HEADER -> PasswordHeaderViewHolder(headerItem)
            ITEM_VIEW_TYPE_ITEM -> AllPasswordsViewHolder(passwordItem)
            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is AccountListItem.AccountItem -> ITEM_VIEW_TYPE_ITEM
            is AccountListItem.AccountHeaderItem -> ITEM_VIEW_TYPE_HEADER
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is AllPasswordsViewHolder -> {
                val password = getItem(position) as AccountListItem.AccountItem
                holder.setData(password.account, btnCopyClickListener)
            }
            is PasswordHeaderViewHolder -> {
                val header = getItem(position) as AccountListItem.AccountHeaderItem
                holder.setData(header.title)
            }
        }



    }

    inner class AllPasswordsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val title = itemView.findViewById<TextView>(R.id.tvListTitle)
        private val username = itemView.findViewById<TextView>(R.id.tvListUsername)
        private val imgCopy = itemView.findViewById<ImageView>(R.id.imgCopy)

        fun setData(account: Account, btnCopyClickListener: ((String) -> Unit)?) {
            title.text = account.websiteName
            username.text = account.userName
            imgCopy.setOnClickListener {btnCopyClickListener?.let { it(account.password) }}
        }
    }

    inner class PasswordHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val passwordTitle = itemView.findViewById<TextView>(R.id.tvPasswordTitle)

        fun setData(header: String){
            passwordTitle.text = header
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<AccountListItem>() {
            override fun areItemsTheSame(oldItem: AccountListItem, newItem: AccountListItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AccountListItem, newItem: AccountListItem): Boolean =
                oldItem == newItem
        }
    }

}





