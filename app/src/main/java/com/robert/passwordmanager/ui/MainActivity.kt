package com.robert.passwordmanager.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.marginBottom
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robert.passwordmanager.R
import com.robert.passwordmanager.databinding.ActivityMainBinding
import com.robert.passwordmanager.models.Account
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    val passwordViewModel: PasswordViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController = navHostFrag.navController

        // connect NavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.addAccountFragment -> {
                    binding.bottomAppBar.visibility = View.GONE
                    binding.navHostFrag.layoutParams
                    binding.fab.visibility = View.GONE
                }
                else -> {
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                }
            }
        }


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            //navController.navigateUp()
            navController.navigate(R.id.addAccountFragment)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getPasswordDetails(result: ActivityResult?): Account {
        val sdf = SimpleDateFormat("dd MMM, yyy")
        val currentDate: String = sdf.format(Date())
        return Account(
            websiteName = result?.data?.getStringExtra("name")!!,
            userName = result.data?.getStringExtra("email")!!,
            category = result.data?.getStringExtra("category")!!,
            password = result.data?.getStringExtra("password")!!,
            date = currentDate
        )
    }
}