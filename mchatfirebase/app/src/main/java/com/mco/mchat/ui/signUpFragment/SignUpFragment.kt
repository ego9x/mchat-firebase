package com.mco.mchat.ui.signUpFragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mco.mchat.R
import com.mco.mchat.data.model.CreateUser
import com.mco.mchat.databinding.FragmentSignUpBinding
import com.mco.mchat.ui.base.BaseFragment
import com.mco.mchat.utils.animationOptions
import com.mco.mchat.utils.showSnackBar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.loading.observe(viewLifecycleOwner, {
            if (it) showLoadingDialog() else hideLoadingDialog()
        })

        viewModel.message.observe(viewLifecycleOwner, {
            binding.root.showSnackBar(it)
        })

        viewModel.isCreatedUser.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.dataStorage.setUserID(it.uid)
            }
            findNavController().navigate(R.id.actionSignUpToChat)
        })
    }

    private fun setupView() {
        with(binding) {
            imgClose.setOnClickListener {
                findNavController().popBackStack()
            }

            btSignUpNow.setOnClickListener {
                viewModel.createAccount(
                    CreateUser(
                        fullName.text.toString(),
                        email.text.toString(),
                        password.text.toString()
                    )
                )
            }
        }
    }
}