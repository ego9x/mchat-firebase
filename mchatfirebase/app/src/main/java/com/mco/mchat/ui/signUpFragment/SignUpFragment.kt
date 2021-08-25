package com.mco.mchat.ui.signUpFragment

import android.os.Bundle
import android.view.View
import com.mco.mchat.R
import com.mco.mchat.databinding.FragmentSignUpBinding
import com.mco.mchat.ui.base.BaseFragment

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
    }
}