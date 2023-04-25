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

class AllPasswordsAdapter(): ListAdapter<PasswordDetails, AllPasswordsAdapter.AllPasswordsViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPasswordsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vault_item, parent, false)
        return AllPasswordsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllPasswordsViewHolder, position: Int) {
        val password = getItem(position)
        holder.setData(password)

    }

    inner class AllPasswordsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val title = itemView.findViewById<TextView>(R.id.tvListTitle)
        private val username = itemView.findViewById<TextView>(R.id.tvListUsername)

        fun setData(password: PasswordDetails) {
            title.text = password.websiteName
            username.text = password.userName
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PasswordDetails>() {
            override fun areItemsTheSame(oldItem: PasswordDetails, newItem: PasswordDetails): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PasswordDetails, newItem: PasswordDetails): Boolean =
                oldItem == newItem
        }
    }

}

