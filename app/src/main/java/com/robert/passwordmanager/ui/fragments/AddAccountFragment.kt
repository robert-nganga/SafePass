package com.robert.passwordmanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robert.passwordmanager.R
import com.robert.passwordmanager.databinding.FragmentAddAccountBinding
import androidx.core.widget.addTextChangedListener
import com.robert.passwordmanager.utils.Validation
import com.robert.passwordmanager.utils.Validation.validatePassword

class AddAccountFragment: Fragment(R.layout.fragment_add_account) {

    private var _binding: FragmentAddAccountBinding? = null
    private val binding get() = _binding!!

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
        passwordFocusListener()
    }

    private fun passwordFocusListener() {
        binding.txtPassword.addTextChangedListener {editable->
            binding.passwordTextField.helperText = validatePassword(editable.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}