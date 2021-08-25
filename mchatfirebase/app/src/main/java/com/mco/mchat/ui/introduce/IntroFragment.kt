package com.mco.mchat.ui.introduce

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mco.mchat.R
import com.mco.mchat.databinding.FragmentIntroBinding
import com.mco.mchat.ui.base.BaseFragment
import com.mco.mchat.utils.animationOptions

class IntroFragment : BaseFragment(R.layout.fragment_intro) {

    private lateinit var binding: FragmentIntroBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroBinding.bind(view)
        setupView()
    }

    private fun setupView() {
        with(binding){
            btSignUp.setOnClickListener {
                findNavController().navigate(R.id.actionIntroToSignUp,null, animationOptions)
            }

            btSignIn.setOnClickListener {
                findNavController().navigate(R.id.actionIntroToSignIn)
            }
        }
    }

}