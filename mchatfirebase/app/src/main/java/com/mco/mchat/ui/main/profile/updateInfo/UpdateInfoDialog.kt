package com.mco.mchat.ui.main.profile.updateInfo

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.mco.mchat.R
import com.mco.mchat.databinding.UpdateInfoDialogBinding

class UpdateInfoDialog : DialogFragment(R.layout.update_info_dialog) {
    private val binding: UpdateInfoDialogBinding by lazy { UpdateInfoDialogBinding.inflate(LayoutInflater.from(context)) }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            it.getString(STATUS)?.let { status ->
                binding.edtStatus.setText(status)
            }
        }
        return AlertDialog.Builder(activity)
            .setView(binding.root)
            .create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        with(binding){
            btUpdate.setOnClickListener {
                setFragmentResult(STATUS_REQUEST_KEY, bundleOf(STATUS_ITEM_KEY to edtStatus.text.toString()))

                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "FriendDialogFragment"
        const val STATUS = "status"
        const val STATUS_REQUEST_KEY = "status_request_key"
        const val STATUS_ITEM_KEY = "status_item_key"

        fun newInstance(param1: String) =
            UpdateInfoDialog().apply {
                arguments = Bundle().apply {
                    putString(STATUS, param1)
                }
            }
    }
}