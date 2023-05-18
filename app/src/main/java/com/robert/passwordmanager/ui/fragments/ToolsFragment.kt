package com.robert.passwordmanager.ui.fragments

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
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.robert.passwordmanager.ui.AccountViewModel
import com.robert.passwordmanager.R
import com.robert.passwordmanager.databinding.FragmentToolsBinding
import com.robert.passwordmanager.ui.MainActivity
import com.robert.passwordmanager.utils.Utilities.copyPasswordToClipboard
import kotlin.math.roundToInt

class ToolsFragment : Fragment() {
    private var isNumbers: Boolean = true
    private var isLetters: Boolean = true
    private var isSymbols: Boolean = true
    private var length: Int = 8

    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel = (activity as MainActivity).accountViewModel

        setupSwitchCheckedChangeListeners()

        binding.slider.addOnChangeListener { _, value, _ ->
            binding.txtLength.text = value.roundToInt().toString()
            length = value.roundToInt()
        }

        binding.btnCopyPassword.setOnClickListener {
            copyPasswordToClipboard(requireContext(), binding.txtGeneratedPassword.text.toString())
        }

        binding.generatePassword.setOnClickListener {
            if(!isNumbers && !isLetters && !isSymbols){
                Toast.makeText(view.context, "Check at least one item", Toast.LENGTH_SHORT).show()
            }else{
                accountViewModel.generatePassword(isLetters, isNumbers , isSymbols, length)
            }
        }

        accountViewModel.generatedPassword.observe(viewLifecycleOwner){ password->
            binding.txtGeneratedPassword.text = password
        }
    }

    private fun setupSwitchCheckedChangeListeners() {
        binding.numbersSwitch.setOnCheckedChangeListener { _, isChecked ->
            isNumbers = isChecked
        }

        binding.lettersSwitch.setOnCheckedChangeListener { _, isChecked ->
            isLetters = isChecked
        }

        binding.symbolsSwitch.setOnCheckedChangeListener { _, isChecked ->
            isSymbols = isChecked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}