package com.mco.mchat.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mco.mchat.utils.SingleLiveEvent
import com.mco.mchat.data.Result

open class BaseViewModel: ViewModel() {

    protected val mSnackBarText = SingleLiveEvent<String>()
    val snackBarText: SingleLiveEvent<String> = mSnackBarText

    private val mDataLoading = SingleLiveEvent<Boolean>()
    val dataLoading: SingleLiveEvent<Boolean> = mDataLoading

    protected fun <T> onResult(mutableLiveData: MutableLiveData<T>? = null, result: Result<T>) {
        when (result) {
            is Result.Loading -> mDataLoading.value = true

            is Result.Error -> {
                mDataLoading.value = false
                result.msg?.let { mSnackBarText.value = it }
            }

            is Result.Success -> {
                mDataLoading.value = false
                result.data?.let { mutableLiveData?.value = it }
                result.msg?.let { mSnackBarText.value = it }
            }
        }
    }
}