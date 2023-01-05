package com.robert.passwordmanager.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.robert.passwordmanager.R

class AddPasswordActivity : AppCompatActivity() {
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var txtName: TextInputEditText
    private lateinit var txtEmail: TextInputEditText
    private lateinit var txtPassword: TextInputEditText
    private lateinit var toolBar: MaterialToolbar
    private var category: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_password)
        setSupportActionBar(findViewById(R.id.topAppBar))
        actionBar?.setDisplayHomeAsUpEnabled(true)

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
            category = adapterCategory.getItem(position)
            Log.i("AddPasswordActivity", category!!)
        }

        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        val fab = findViewById<ExtendedFloatingActionButton>(R.id.extended_fab)

        fab.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(txtName.text)
                || TextUtils.isEmpty(txtEmail.text)
                || TextUtils.isEmpty(txtName.text) ){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                replyIntent.putExtra("name", txtName.text.toString())
                replyIntent.putExtra("email", txtEmail.text.toString())
                replyIntent.putExtra("password", txtPassword.text.toString())
                replyIntent.putExtra("category", category!!)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

    }
}