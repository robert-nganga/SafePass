package com.robert.passwordmanager.screens.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.PasswordsAdapter
import com.robert.passwordmanager.R
import com.robert.passwordmanager.models.PasswordDetails
import com.robert.passwordmanager.models.Section

class AllPasswordsAdapter(val context: Context,
                          private val deletePassword: (PasswordDetails) -> Unit): RecyclerView.Adapter<AllPasswordsAdapter.AllPasswordsViewHolder>() {
    private val sections = ArrayList<Section>()

    fun addSections(newList: ArrayList<Section>) {
        sections.clear()
        sections.addAll(newList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPasswordsViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.rv_section, parent, false)
        return AllPasswordsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllPasswordsViewHolder, position: Int) {
        val section = sections[position]
        holder.setData(section)

    }

    override fun getItemCount(): Int = sections.size

    inner class AllPasswordsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val sectionTitle = itemView.findViewById<TextView>(R.id.sectionTitle)
        private val childRecyclerView = itemView.findViewById<RecyclerView>(R.id.childRecyclerview)

        fun setData(section: Section) {
            sectionTitle.text = section.sectionTitle
            val childRecyclerAdapter = PasswordsAdapter(context, deletePassword)
            childRecyclerAdapter.updateList(section.itemList)
            childRecyclerView.adapter = childRecyclerAdapter
        }
    }

}

