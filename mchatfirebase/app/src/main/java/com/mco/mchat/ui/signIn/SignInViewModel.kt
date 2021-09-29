package com.mco.mchat.ui.signIn

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.mco.mchat.data.model.Login
import com.mco.mchat.data.repo.AuthRepository
import com.mco.mchat.ui.base.BaseViewModel
import com.mco.mchat.data.Result
import com.mco.mchat.data.storage.PrefsDataStoreManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInViewModel(private val authRepository: AuthRepository,val dataStorage: PrefsDataStoreManager): BaseViewModel() {

    private val _isLoggedInEvent = MutableLiveData<FirebaseUser?>()
    val isLoggedInEvent: LiveData<FirebaseUser?> = _isLoggedInEvent

    fun signIn(login: Login) {
        if(validate(login)){
            viewModelScope.launch {
                mLoading.value = true
                authRepository.loginUser(login) { result: Result<FirebaseUser> ->
                    if (result is Result.Success) _isLoggedInEvent.value = result.data
                    else _isLoggedInEvent.value = null
                }
                mLoading.value = false
            }
        }

    }

    private fun validate(login: Login): Boolean {
        if (login.email.isEmpty()) {
            message.value = "Địa chỉ email trống !"
            return false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(login.email).matches()){
            mMessage.value = "Địa chỉ email sai định dạng !"
            return false
        }
        else if (login.password.isEmpty()) {
            message.value = "Mật khẩu trống !"
            return false
        }
        return true
    }
}