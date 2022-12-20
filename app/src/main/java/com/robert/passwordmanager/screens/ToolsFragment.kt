package com.robert.passwordmanager.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.robert.passwordmanager.R

class ToolsFragment : Fragment() {

    private lateinit var txtPassword: TextView
    private lateinit var btnCopy: ImageButton
    private lateinit var txtLength: TextView
    private lateinit var slider: Slider
    private lateinit var numbersSwitch: SwitchMaterial
    private lateinit var lettersSwitch: SwitchMaterial
    private lateinit var symbolsSwitch: SwitchMaterial
    private lateinit var btnGenerate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tools, container, false)
        return view
    }

}