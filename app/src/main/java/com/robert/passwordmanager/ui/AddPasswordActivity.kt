package com.robert.passwordmanager.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.widget.addTextChangedListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.robert.passwordmanager.R

class AddPasswordActivity : AppCompatActivity() {
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var txtName: TextInputEditText
    private lateinit var lytName: TextInputLayout
    private lateinit var txtEmail: TextInputEditText
    private lateinit var lytEmail: TextInputLayout
    private lateinit var txtPassword: TextInputEditText
    private lateinit var lytPassword: TextInputLayout
    private lateinit var lytCategory: TextInputLayout
    private lateinit var toolBar: MaterialToolbar
    private var category: String? = null
    private var hasNoSelection = true

    val passwordViewModel: PasswordViewModel by viewModels { PasswordViewModel.Factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_password)
        setSupportActionBar(findViewById(R.id.topAppBar))
        actionBar?.setDisplayHomeAsUpEnabled(true)
        lytName = findViewById(R.id.textField)
        lytEmail = findViewById(R.id.emailsTextField)
        lytCategory = findViewById(R.id.spinner)
        lytPassword = findViewById(R.id.passwordTextField)

        toolBar = findViewById(R.id.topAppBar)
        toolBar.setNavigationOnClickListener {
            finish()
        }

        autoCompleteTextView = findViewById(R.id.autoCompleteTextViews)
        val categories = resources.getStringArray(R.array.categories)

        val adapterCategory = ArrayAdapter(this,
            R.layout.dropdown_item,
            categories)
        autoCompleteTextView.setAdapter(adapterCategory)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            hasNoSelection = false
            autoCompleteTextView.error = null
            category = adapterCategory.getItem(position)
            Log.i("AddPasswordActivity", category!!)
        }

        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        txtPassword.addTextChangedListener { editable ->
            if (editable != null) {
                validatePassword(editable)
            }
        }
        val fab = findViewById<ExtendedFloatingActionButton>(R.id.extended_fab)

        fab.setOnClickListener {
            validateTextInputs()
        }

    }

    private fun validatePassword(editable: Editable) {
        val result = passwordViewModel.evaluatePassword(editable.toString())
        if(result<0.5){
            txtPassword.error = "Weak password"
        }else{
            txtPassword.error = null
        }
    }


    private fun validateTextInputs() {
        when{
            TextUtils.isEmpty(txtName.text) -> {
                txtName.error = "Must not be empty"
            }
            TextUtils.isEmpty(txtEmail.text) -> {
                txtEmail.error = "Must not be empty"
            }
            hasNoSelection -> {
                autoCompleteTextView.error = "Select"

            }
            TextUtils.isEmpty(txtPassword.text) -> {
                txtPassword.error = "Must not be empty"
            }
            else -> {
                passIntentExtras()
            }
        }
    }

    private fun passIntentExtras() {
        val replyIntent = Intent()
        replyIntent.putExtra("name", txtName.text.toString())
        replyIntent.putExtra("email", txtEmail.text.toString())
        replyIntent.putExtra("password", txtPassword.text.toString())
        replyIntent.putExtra("category", category)
        setResult(Activity.RESULT_OK, replyIntent)
        finish()
    }
}