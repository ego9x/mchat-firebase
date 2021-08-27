package com.mco.mchat.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mco.mchat.utils.SingleLiveEvent
import com.mco.mchat.data.Result

open class BaseViewModel: ViewModel() {

    protected val mMessage = SingleLiveEvent<String>()
    val message: SingleLiveEvent<String> = mMessage

    protected val mLoading = SingleLiveEvent<Boolean>()
    val loading: SingleLiveEvent<Boolean> = mLoading

    protected fun <T> onResult(mutableLiveData: MutableLiveData<T>? = null, result: Result<T>) {
        when (result) {
            is Result.Loading -> mLoading.value = true

            is Result.Error -> {
                mLoading.value = false
                result.msg?.let { mMessage.value = it }
            }

            is Result.Success -> {
                mLoading.value = false
                result.data?.let { mutableLiveData?.value = it }
                result.msg?.let { mMessage.value = it }
            }
        }
    }
}