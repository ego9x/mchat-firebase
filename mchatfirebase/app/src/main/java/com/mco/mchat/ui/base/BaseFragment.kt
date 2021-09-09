package com.mco.mchat.ui.base

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.mco.mchat.R
import com.mco.mchat.ui.main.MainActivity
import permissions.dispatcher.PermissionRequest

open class BaseFragment(resLayout: Int): Fragment(resLayout) {

    fun showLoadingDialog(){
        (requireActivity() as MainActivity).showLoadingDialog()
    }

    fun hideLoadingDialog(){
        (requireActivity() as MainActivity).hideLoadingDialog()
    }

    fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        AlertDialog.Builder(requireContext())
            .setPositiveButton(getString(R.string.allow)) { _, _ -> request.proceed() }
            .setNegativeButton(getString(R.string.deny)) { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }
}