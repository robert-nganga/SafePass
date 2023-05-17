package com.robert.passwordmanager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.robert.passwordmanager.R
import com.robert.passwordmanager.models.Account
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class AddAccountFragmentTest{

    @Test
    fun accountDetails_displayedInUI(){
        val account = Account(websiteName = "Dropbox", id = 2, userName = "mercy@yahoo.com", category = "Website", password = "gdj237Tads!AS142@hsAg",
            passwordStrength = 0.8, passwordStrengthLabel = "Very strong password", date = "10 may 2023")

        val bundle = AddAccountFragmentArgs(account.id).toBundle()
        launchFragmentInContainer<AddAccountFragment>(bundle, R.style.Theme_PasswordManager)
    }
}