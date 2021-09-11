package com.mco.mchat.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mco.mchat.utils.SingleLiveEvent
import com.mco.mchat.data.Result
import com.mco.mchat.data.storage.PrefsDataStoreManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseViewModel: ViewModel(), KoinComponent {
    val prefsDataStoreManager: PrefsDataStoreManager by inject()

    lateinit var userID: String

    protected val mMessage = SingleLiveEvent<String>()
    val message: SingleLiveEvent<String> = mMessage

    protected val mLoading = SingleLiveEvent<Boolean>()
    val loading: SingleLiveEvent<Boolean> = mLoading

    init {
        viewModelScope.launch {
            prefsDataStoreManager.userID.collect { userId ->
                userID = userId

            }
        }
    }

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