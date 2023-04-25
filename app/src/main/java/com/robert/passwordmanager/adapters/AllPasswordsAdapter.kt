package com.robert.passwordmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.R
import com.robert.passwordmanager.models.PasswordDetails

class AllPasswordsAdapter(): RecyclerView.Adapter<AllPasswordsAdapter.AllPasswordsViewHolder>() {
    private val passwords = ArrayList<PasswordDetails>()

    fun addSections(newList: ArrayList<PasswordDetails>) {
        passwords.clear()
        passwords.addAll(newList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPasswordsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vault_item, parent, false)
        return AllPasswordsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllPasswordsViewHolder, position: Int) {
        val password = passwords[position]
        holder.setData(password)

    }

    override fun getItemCount(): Int = passwords.size

    inner class AllPasswordsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val title = itemView.findViewById<TextView>(R.id.tvListTitle)
        private val username = itemView.findViewById<TextView>(R.id.tvListUsername)

        fun setData(password: PasswordDetails) {
            title.text = password.websiteName
            username.text = password.userName
        }
    }

}

