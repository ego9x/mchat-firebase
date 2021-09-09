package com.mco.mchat.ui.introduce

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mco.mchat.R
import com.mco.mchat.data.storage.PrefsDataStoreManager
import com.mco.mchat.databinding.FragmentIntroBinding
import com.mco.mchat.ui.base.BaseFragment
import com.mco.mchat.utils.animationOptions
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class IntroFragment : BaseFragment(R.layout.fragment_intro) {

    private lateinit var binding: FragmentIntroBinding
    private val viewModel: IntroViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroBinding.bind(view)
        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.userID.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()){
                findNavController().navigate(R.id.actionIntroToChat)
            }
        })
    }

    private fun setupView() {
        with(binding){
            btSignUp.setOnClickListener {
                findNavController().navigate(R.id.actionIntroToSignUp,null, animationOptions)
            }

            btSignIn.setOnClickListener {
                findNavController().navigate(R.id.actionIntroToSignIn, null, animationOptions)
            }
        }
    }

}