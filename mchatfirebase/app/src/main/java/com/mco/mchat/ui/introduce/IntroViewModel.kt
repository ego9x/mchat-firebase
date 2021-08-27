package com.mco.mchat.ui.introduce

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mco.mchat.data.storage.PrefsDataStoreManager
import com.mco.mchat.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class IntroViewModel(val prefsDataStoreManager: PrefsDataStoreManager): BaseViewModel() {

    private var _userID = MutableLiveData<String>()
    val userID: LiveData<String> = _userID

    init {
        viewModelScope.launch {
            prefsDataStoreManager.userID.collect {
                _userID.value = it
            }
        }
    }
}