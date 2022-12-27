package com.robert.passwordmanager

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robert.passwordmanager.models.PasswordDetails
import com.robert.passwordmanager.screens.AddPasswordActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var bottomAppBar: BottomAppBar

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            passwordViewModel.insert(getPasswordDetails(it))
            Log.i("MainActivity", "Password added successfully")
        }
    }

    private val passwordViewModel: PasswordViewModel by viewModels { PasswordViewModel.Factory}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.bottomNavigationView)
        bottomAppBar = findViewById(R.id.bottomAppBar)
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController = navHostFrag.navController

        // connect NavigationView with NavController
        bottomNavView.setupWithNavController(navController)


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddPasswordActivity::class.java)
            getResult.launch(intent)

        }
    }

    private fun getPasswordDetails(result: ActivityResult?): PasswordDetails {
        val sdf = SimpleDateFormat("dd MMM, yyy - HH:mm")
        val currentDate: String = sdf.format(Date())
        val password = PasswordDetails(
            websiteName = result?.data?.getStringExtra("name")!!,
            userName = result.data?.getStringExtra("email")!!,
            category = result.data?.getStringExtra("category")!!,
            password = result.data?.getStringExtra("password")!!,
            date = currentDate
        )
        return password
    }
}