package com.robert.passwordmanager


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
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
        recentsViewHolder.setListeners()
    }

    override fun getItemCount(): Int = passwordList.size

    fun updateList(newList: List<PasswordDetails>){
        passwordList.clear()
        passwordList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class RecentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private var currentPosition: Int = -1
        private var currentPassword: PasswordDetails? = null
        private val txtName = itemView.findViewById<TextView>(R.id.nameTv)
        private val txtEmail = itemView.findViewById<TextView>(R.id.emailTv)
        private val txtPassword = itemView.findViewById<TextView>(R.id.passwordTv)
        private val btnPassword = itemView.findViewById<ImageButton>(R.id.passwordVisibility)
        private val btnMoreOptions = itemView.findViewById<ImageButton>(R.id.moreOptions)
        private val icVisibilityOff = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_visibility_off_24, null)
        private val icVisibilityOn = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_baseline_visibility_24, null)
        private var isPasswordVisible = false

        fun setData(passwordDetails: PasswordDetails, position: Int){
            txtName.text = passwordDetails.websiteName
            txtEmail.text = passwordDetails.userName
            txtPassword.text = passwordDetails.password
            this.currentPosition = position
            this.currentPassword = passwordDetails
            btnPassword.setImageDrawable(icVisibilityOff)
        }

        fun setListeners() {
            btnPassword.setOnClickListener(this)
            btnMoreOptions.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when(view!!.id){
                R.id.passwordVisibility -> handlePasswordVisibility()
                R.id.moreOptions -> displayMenuOptions(view)
            }
        }

        private fun handlePasswordVisibility() {
            if (isPasswordVisible){
                txtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btnPassword.setImageDrawable(icVisibilityOn)
                isPasswordVisible = !isPasswordVisible

            }else{
                txtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnPassword.setImageDrawable(icVisibilityOff)
                isPasswordVisible = !isPasswordVisible
            }
        }


        private fun displayMenuOptions(view: View) {
            val popUpMenu = PopupMenu(view.context, view)
            popUpMenu.inflate(R.menu.pop_up_menu)
            popUpMenu.setOnMenuItemClickListener(this)
            popUpMenu.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item!!.itemId){
                R.id.action_copy -> copyPasswordToClipboard()
                R.id.action_delete -> deletePassword()
            }
            return false
        }

        private fun deletePassword() {

        }

        private fun copyPasswordToClipboard() {
            val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("simple text", txtPassword.text.toString())
            clipboard.setPrimaryClip(clip)
            // Only show a toast for Android 12 and lower.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                Toast.makeText(itemView.context, "Copied Password", Toast.LENGTH_SHORT).show()
        }
    }
}