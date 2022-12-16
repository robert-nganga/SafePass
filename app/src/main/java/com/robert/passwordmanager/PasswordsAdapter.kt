package com.robert.passwordmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.models.PasswordDetails

class PasswordsAdapter(val context: Context): RecyclerView.Adapter<PasswordsAdapter.RecentsViewHolder>() {

    private val passwordList = ArrayList<PasswordDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentsViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.recent_rv_item, parent, false)
        return RecentsViewHolder(itemView)
    }

    override fun onBindViewHolder(recentsViewHolder: RecentsViewHolder, position: Int) {
        val password = passwordList[position]
        recentsViewHolder.setData(password, position)
    }

    override fun getItemCount(): Int = passwordList.size

    fun updateList(newList: List<PasswordDetails>){
        passwordList.clear()
        passwordList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class RecentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var currentPosition: Int = -1
        private var currentPassword: PasswordDetails? = null
        private val txtName = itemView.findViewById<TextView>(R.id.nameTv)
        private val txtEmail = itemView.findViewById<TextView>(R.id.emailTv)
        private val txtPassword = itemView.findViewById<TextView>(R.id.passwordTv)

        fun setData(passwordDetails: PasswordDetails, position: Int){
            txtName.text = passwordDetails.websiteName
            txtEmail.text = passwordDetails.userName
            txtPassword.text = passwordDetails.password
            this.currentPosition = position
            this.currentPassword = passwordDetails
        }
    }
}