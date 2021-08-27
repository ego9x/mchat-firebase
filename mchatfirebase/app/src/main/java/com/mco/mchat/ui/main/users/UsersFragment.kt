package com.mco.mchat.ui.main.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mco.mchat.R
import com.mco.mchat.databinding.FragmentUsersBinding
import com.mco.mchat.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment : BaseFragment(R.layout.fragment_users) {
    lateinit var binding : FragmentUsersBinding
    private val viewModel: UsersViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUsersBinding.bind(view)
        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {

    }

    private fun setupView() {

    }


}