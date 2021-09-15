package com.mco.mchat.ui.main.profile

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.mco.mchat.R
import com.mco.mchat.databinding.FragmentProfileBinding
import com.mco.mchat.ui.base.BaseFragment
import com.mco.mchat.ui.main.profile.adapter.UserItem
import com.mco.mchat.ui.main.profile.friend.FriendDialogFragment
import com.mco.mchat.utils.DialogHelper
import com.mco.mchat.utils.animationOptions
import com.mco.mchat.utils.Ext.loadImageFromStorage
import com.mco.mchat.utils.Ext.openAppSettings
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.PermissionsRequester
import permissions.dispatcher.ktx.constructPermissionsRequest

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModel()
    lateinit var userAdapter: ItemAdapter<UserItem>
    private lateinit var storagePermissionsRequester: PermissionsRequester

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        setupView()
        setupViewModel()

        storagePermissionsRequester = constructPermissionsRequest(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            onShowRationale = ::onStorageShowRationale,
            onPermissionDenied = ::onStorageDenied,
            onNeverAskAgain = ::onStorageNeverAskAgain,
            requiresPermission = ::openGallery
        )

        lifecycleScope.launch {
            viewModel.prefsDataStoreManager.userID.collect { userId ->
                if (userId.isEmpty())
                    findNavController().navigate(R.id.actionLogout, null, animationOptions)
            }
        }
    }

    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                requireContext().loadImageFromStorage(it)?.apply {
                    viewModel.changeUserImage(this)
                }
            }
        }

    private fun setupViewModel() {
        viewModel.userInfo.observe(viewLifecycleOwner, { user ->
            with(binding) {
                user.profileImageUrl.let {
                    if (it.isNotEmpty()) {
                        avatar.load(it)
                    } else avatar.text = user.displayName
                }
                tvUserName.text = user.displayName
                tvStatus.text = user.status
            }
        })

        viewModel.friends.observe(viewLifecycleOwner, {
            userAdapter.set(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            if(it) showLoadingDialog() else hideLoadingDialog()
        })
    }

    private fun setupView() {
        with(binding) {
            userAdapter = ItemAdapter()
            rcUser.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            val fastAdapter = FastAdapter.with(userAdapter)

            rcUser.adapter = fastAdapter

            fastAdapter.onClickListener = { _, _, item, _ ->
                FriendDialogFragment.newInstance(item.user.info.id)
                    .show(childFragmentManager, FriendDialogFragment.TAG)
                false
            }

            avatar.setOnClickListener {
                openGallery()
            }

            btLogout.setOnClickListener {
                viewModel.logout()
            }

        }

    }

    private fun openGallery() = registerForActivityResult.launch("image/*")

    private fun onStorageDenied() {}

    private fun onStorageShowRationale(request: PermissionRequest) {
        showRationaleDialog(R.string.rationale_storage, request)
    }

    private fun onStorageNeverAskAgain() {
        DialogHelper.alert(
            requireContext(),
            getString(R.string.storage_app_settings),
            { _, _ -> requireContext().openAppSettings() }
        ) { dialog, _ -> dialog.dismiss() }.show()
    }


}