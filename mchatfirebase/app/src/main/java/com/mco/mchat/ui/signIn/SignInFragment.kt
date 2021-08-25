package com.mco.mchat.ui.signIn

import android.os.Bundle
import android.view.View
import com.mco.mchat.R
import com.mco.mchat.databinding.FragmentSignInBinding
import com.mco.mchat.ui.base.BaseFragment

class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
    }
}