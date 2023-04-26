package com.robert.passwordmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.R
import com.robert.passwordmanager.models.PasswordDetails
import com.robert.passwordmanager.models.PasswordItem
import com.robert.passwordmanager.utils.Contants.ITEM_VIEW_TYPE_HEADER
import com.robert.passwordmanager.utils.Contants.ITEM_VIEW_TYPE_ITEM
import java.lang.ClassCastException

class AllPasswordsAdapter(): ListAdapter<PasswordItem, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val passwordItem = LayoutInflater.from(parent.context).inflate(R.layout.vault_item, parent, false)
        val headerItem = LayoutInflater.from(parent.context).inflate(R.layout.password_title, parent, false)
        return when (viewType){
            ITEM_VIEW_TYPE_HEADER -> PasswordHeaderViewHolder(headerItem)
            ITEM_VIEW_TYPE_ITEM -> AllPasswordsViewHolder(passwordItem)
            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is PasswordItem.Password -> ITEM_VIEW_TYPE_ITEM
            is PasswordItem.PasswordTitle -> ITEM_VIEW_TYPE_HEADER
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is AllPasswordsViewHolder -> {
                val password = getItem(position) as PasswordItem.Password
                holder.setData(password.pass)
            }
            is PasswordHeaderViewHolder -> {
                val header = getItem(position) as PasswordItem.PasswordTitle
                holder.setData(header.title)
            }
        }



    }

    inner class AllPasswordsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val title = itemView.findViewById<TextView>(R.id.tvListTitle)
        private val username = itemView.findViewById<TextView>(R.id.tvListUsername)

        fun setData(password: PasswordDetails) {
            title.text = password.websiteName
            username.text = password.userName
        }
    }

    inner class PasswordHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val passwordTitle = itemView.findViewById<TextView>(R.id.tvPasswordTitle)

        fun setData(header: String){
            passwordTitle.text = header
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PasswordItem>() {
            override fun areItemsTheSame(oldItem: PasswordItem, newItem: PasswordItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PasswordItem, newItem: PasswordItem): Boolean =
                oldItem == newItem
        }
    }

}



