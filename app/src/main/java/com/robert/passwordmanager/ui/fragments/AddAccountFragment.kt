package com.robert.passwordmanager.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.robert.passwordmanager.R
import com.robert.passwordmanager.databinding.FragmentAddAccountBinding
import com.robert.passwordmanager.ui.MainActivity
import com.robert.passwordmanager.ui.PasswordViewModel


class AddAccountFragment: Fragment(R.layout.fragment_add_account) {

    private var _binding: FragmentAddAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var passwordViewModel: PasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordViewModel = (activity as MainActivity).passwordViewModel

        val categories = resources.getStringArray(R.array.categories)

        val categoryAdapter = ArrayAdapter(requireContext(),
            R.layout.dropdown_item,
            categories)
        binding.categorySpinner.setAdapter(categoryAdapter)


        binding.extendedFab.setOnClickListener {
            validateInputs()
        }

    }


    private fun validateInputs(){
        val passwordValid = passwordViewModel.validatePassword(binding.passwordText.text.toString())
        when{
            TextUtils.isEmpty(binding.nameText.text) -> {
                binding.nameContainer.helperText = "Required"
            }
            TextUtils.isEmpty(binding.userNameText.text) -> {
                binding.userNameContainer.helperText = "Required"
            }
            TextUtils.isEmpty(binding.categorySpinner.text) -> {
                binding.categoryContainer.helperText = "Required"
            }
            passwordValid != null -> {
                binding.passwordContainer.helperText = passwordValid
            }
            else ->{
                val selectedCategory: String = (binding.categoryContainer.editText as AutoCompleteTextView).text.toString()
                passwordViewModel.createAccount(
                    name = binding.nameText.text.toString(),
                    username = binding.userNameText.text.toString(),
                    category = selectedCategory,
                    password = binding.passwordText.text.toString()
                )
                findNavController().popBackStack()
            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}