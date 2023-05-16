package com.robert.passwordmanager.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.databinding.VaultItemBinding
import com.robert.passwordmanager.models.Account

class AccountsAdapter(): ListAdapter<Account, AccountsAdapter.RecentsViewHolder >(diffUtil) {

    private var btnCopyClickListener: ((String)->Unit)? = null

    fun setCopyClickListener(listener: (String)-> Unit){
        btnCopyClickListener = listener
    }

    private var itemClickListener: ((Account)->Unit)? = null

    fun itemClickListener(listener: (Account)-> Unit){
        itemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentsViewHolder {
        val binding = VaultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentsViewHolder(binding)
    }

    override fun onBindViewHolder(recentsViewHolder: RecentsViewHolder, position: Int) {
        val account = getItem(position)
        recentsViewHolder.itemView.setOnClickListener { itemClickListener?.let { it(account) } }
        recentsViewHolder.setData(account, btnCopyClickListener)
    }



    inner class RecentsViewHolder(private val binding: VaultItemBinding): RecyclerView.ViewHolder(binding.root){

        fun setData(account: Account, btnCopyClickListener: ((String) -> Unit)?){
            binding.tvListTitle.text = account.websiteName
            binding.tvListUsername.text = account.userName
            binding.imgCopy.setOnClickListener {btnCopyClickListener?.let { it(account.password) }}
        }

    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem == newItem
        }
    }
}