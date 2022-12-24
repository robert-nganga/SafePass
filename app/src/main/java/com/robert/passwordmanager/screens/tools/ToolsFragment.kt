package com.robert.passwordmanager.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.robert.passwordmanager.PasswordViewModel
import com.robert.passwordmanager.R
import kotlin.math.roundToInt

class ToolsFragment : Fragment() {

    private lateinit var txtPassword: TextView
    private lateinit var btnCopy: ImageButton
    private lateinit var txtLength: TextView
    private lateinit var slider: Slider
    private lateinit var btnGenerate: Button
    private var isNumbers: Boolean = true
    private var isLetters: Boolean = true
    private var isSymbols: Boolean = true
    private var length: Int = 8

    private val passwordViewModel: PasswordViewModel by viewModels { PasswordViewModel.Factory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tools, container, false)
        txtLength = view.findViewById(R.id.txtLength)
        txtPassword = view.findViewById(R.id.txtGeneratedPassword)

        val numbersSwitch = view.findViewById<SwitchMaterial>(R.id.numbersSwitch)
        numbersSwitch.setOnCheckedChangeListener { _, isChecked ->
            isNumbers = isChecked
        }

        val lettersSwitch = view.findViewById<SwitchMaterial>(R.id.lettersSwitch)
        lettersSwitch.setOnCheckedChangeListener { _, isChecked ->
            isLetters = isChecked
        }

        val symbolsSwitch = view.findViewById<SwitchMaterial>(R.id.symbolsSwitch)
        symbolsSwitch.setOnCheckedChangeListener { _, isChecked ->
            isSymbols = isChecked
        }

        slider = view.findViewById(R.id.slider)
        slider.addOnChangeListener { _, value, _ ->
            txtLength.text = value.roundToInt().toString()
            length = value.roundToInt()
        }

        btnCopy = view.findViewById(R.id.btnCopyPassword)
        btnCopy.setOnClickListener {
            copyGeneratedPassword(view)
        }

        btnGenerate = view.findViewById(R.id.generatePassword)
        btnGenerate.setOnClickListener {
            passwordViewModel.generatePassword(isLetters, isNumbers , isSymbols, length)
        }

        passwordViewModel.generatedPassword.observe(viewLifecycleOwner){ password->
            txtPassword.text = password
        }
        return view
    }

    private fun copyGeneratedPassword(view: View) {
        val clipboard = view.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val password = txtPassword.text
        val clip: ClipData = ClipData.newPlainText("password", password)
        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
            Toast.makeText(view.context, "Copied Password", Toast.LENGTH_SHORT).show()
    }


}