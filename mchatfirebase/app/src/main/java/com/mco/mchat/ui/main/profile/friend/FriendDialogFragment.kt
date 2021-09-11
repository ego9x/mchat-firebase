package com.mco.mchat.ui.main.profile.friend

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.mco.mchat.databinding.FriendDialogFragmentLayoutBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.app.AlertDialog
import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import coil.load
import com.mco.mchat.R


class FriendDialogFragment : DialogFragment(R.layout.friend_dialog_fragment_layout) {

    private val binding: FriendDialogFragmentLayoutBinding by lazy { FriendDialogFragmentLayoutBinding.inflate(LayoutInflater.from(context)) }
    private val viewModel: FriendDialogViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            it.getString(USER_ID)?.let { userID ->
                viewModel.setupProfile(userID)
            }
        }
        setupViewModel()
        return AlertDialog.Builder(activity)
            .setView(binding.root)
            .create()
    }

    private fun setupViewModel() {
        viewModel.layoutState.observe(this, {
            with(binding.btNegative){
                when(it){
                    LayoutState.IS_FRIEND -> {
                        text = getString(R.string.remove_friend)
                        setOnClickListener { viewModel.removeFriendPressed() }
                    }
                    LayoutState.NOT_FRIEND -> {
                        text = getString(R.string.add_friend)
                        setOnClickListener { viewModel.addFriendPressed() }
                    }
                    else -> {
                        text = getString(R.string.request_send)
                        isEnabled = false
                        setTextColor(Color.GRAY)
                        setBackgroundResource(R.drawable.button_style_border_gray)
                    }
                }
            }
        })

        viewModel.otherUser.observe(this, {
            binding.apply {
                avatar.load(it.info.profileImageUrl)
                tvUserName.text = it.info.displayName
                tvStatus.text = it.info.status
                btPositive.setOnClickListener {
                    this@FriendDialogFragment.dismissAllowingStateLoss()

                }
            }
        })
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        const val TAG = "FriendDialogFragment"
        const val USER_ID = "user_id"

        fun newInstance(param1: String) =
            FriendDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, param1)
                }
            }
    }

}