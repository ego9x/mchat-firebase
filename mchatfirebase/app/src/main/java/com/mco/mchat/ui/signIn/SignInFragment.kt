package com.mco.mchat.ui.signIn

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mco.mchat.R
import com.mco.mchat.data.model.Login
import com.mco.mchat.data.storage.PrefsDataStoreManager
import com.mco.mchat.databinding.FragmentSignInBinding
import com.mco.mchat.ui.base.BaseFragment
import com.mco.mchat.utils.animationOptions
import com.mco.mchat.utils.showSnackBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val viewModel: SignInViewModel by viewModel()

    private lateinit var binding: FragmentSignInBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.loading.observe(viewLifecycleOwner, {
            if (it) showLoadingDialog() else hideLoadingDialog()
        })

        viewModel.message.observe(viewLifecycleOwner, { text ->
            binding.root.showSnackBar(text)
        })

        viewModel.isLoggedInEvent.observe(viewLifecycleOwner, {
            it?.let {
                lifecycleScope.launch {
                    viewModel.dataStorage.setUserID(it.uid)
                }
                findNavController().navigate(R.id.actionSignInToChat)
            }
        })
    }

    private fun setupView() {
        with(binding) {
            imgClose.setOnClickListener {
                findNavController().popBackStack()
            }

            btSignIn.setOnClickListener {
                viewModel.signIn(Login(email.text.toString(), password.text.toString()))
            }
        }
    }
}