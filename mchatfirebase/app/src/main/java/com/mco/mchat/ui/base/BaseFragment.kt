package com.mco.mchat.ui.base

import androidx.fragment.app.Fragment
import com.mco.mchat.ui.main.MainActivity

open class BaseFragment(resLayout: Int): Fragment(resLayout) {
    fun showLoadingDialog(){
        (requireActivity() as MainActivity).showLoadingDialog()
    }

    fun hideLoadingDialog(){
        (requireActivity() as MainActivity).hideLoadingDialog()
    }
}