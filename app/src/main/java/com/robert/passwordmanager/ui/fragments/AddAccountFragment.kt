package com.robert.passwordmanager.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robert.passwordmanager.R
import com.robert.passwordmanager.databinding.FragmentAddAccountBinding
import com.robert.passwordmanager.models.Account
import com.robert.passwordmanager.ui.MainActivity
import com.robert.passwordmanager.ui.PasswordViewModel


class AddAccountFragment: Fragment(R.layout.fragment_add_account) {

    private var _binding: FragmentAddAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var passwordViewModel: PasswordViewModel
    private val args: AddAccountFragmentArgs by navArgs()

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
        val isNewAccount = (args.id == -1)

        if (!isNewAccount){
            binding.toolBar.title = resources.getString(R.string.update_page_title)
            binding.extendedFab.text = resources.getString(R.string.extended_fab_update_label)
            passwordViewModel.setId(args.id)

            //Observe account
            passwordViewModel.account.observe(viewLifecycleOwner){ account->
                initializeAccountDetails(account)
            }
        }
        setupAutoCompleteTextView()

        //Input validation
        nameFocusListener()
        usernameFocusListener()
        categoryFocusListener()
        passwordTextChangeListener()


        binding.extendedFab.setOnClickListener {
            validateInputs()
        }
    }

    private fun passwordTextChangeListener() {
        binding.passwordText.addTextChangedListener{editable->
            val strength = passwordViewModel.evaluatePassword(editable.toString())
            val helperView = binding.passwordContainer.findViewById<View>(com.google.android.material.R.id.textinput_helper_text)
            val tvHelper = helperView as TextView

            if (editable.toString().length > 8){
                binding.passwordContainer.helperText = when (strength) {
                    in 0.0 .. 0.5 -> {
                        tvHelper.setTextColor(Color.RED)
                        "Weak password"
                    }
                    in 0.5 .. 0.7 -> {
                        tvHelper.setTextColor(Color.YELLOW)
                        "Strong password"
                    }
                    in 0.7 .. 1.0 -> {
                        tvHelper.setTextColor(Color.GREEN)
                        "Very strong password"
                    }
                    else -> {""}
                }
            }else{
                binding.passwordContainer.helperText = "Must contain more than 8 characters"
            }
        }
    }

    private fun categoryFocusListener() {
        binding.categorySpinner.addTextChangedListener{editable->
            if (editable.toString().isNotEmpty()){
                binding.categoryContainer.helperText = null
            }else{
                binding.categoryContainer.helperText = "Required"
            }
        }
    }

    private fun usernameFocusListener() {
        binding.userNameText.addTextChangedListener{editable->
            if (editable.toString().isNotEmpty()){
                binding.userNameContainer.helperText = null
            }else{
                binding.userNameContainer.helperText = "Required"
            }
        }
    }

    private fun nameFocusListener() {
        binding.nameText.addTextChangedListener{editable->
            if (editable.toString().isNotEmpty()){
                binding.nameContainer.helperText = null
            }else{
                binding.nameContainer.helperText = "Required"
            }
        }
    }

    private fun setupAutoCompleteTextView() {
        val categories = resources.getStringArray(R.array.categories)

        val categoryAdapter = ArrayAdapter(requireContext(),
            R.layout.dropdown_item,
            categories)
        binding.categorySpinner.setAdapter(categoryAdapter)
    }

    private fun initializeAccountDetails(account: Account){
        binding.apply {
            nameText.setText(account.websiteName)
            userNameText.setText(account.userName)
            categorySpinner.setText(account.category)
            passwordText.setText(account.password)

        }
    }

    private fun getAccount(): Account {
        val selectedCategory: String = (binding.categoryContainer.editText as AutoCompleteTextView).text.toString()
        return passwordViewModel.createAccount(
            name = binding.nameText.text.toString(),
            username = binding.userNameText.text.toString(),
            category = selectedCategory,
            password = binding.passwordText.text.toString()
        )
    }

    private fun validateInputs(){
        val passwordValid = binding.passwordText.text.toString().length > 8
        val nameValid = binding.nameContainer.helperText == null
        val userNameValid = binding.userNameContainer.helperText == null
        val categoryValid = binding.categoryContainer.helperText == null

        if (passwordValid && nameValid && userNameValid && categoryValid) {
            val account = getAccount()
            if (args.id == -1){
                passwordViewModel.insertAccount(account)
            }else{
                passwordViewModel.updateAccount(account.copy(id = args.id))
            }
            findNavController().popBackStack()
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}