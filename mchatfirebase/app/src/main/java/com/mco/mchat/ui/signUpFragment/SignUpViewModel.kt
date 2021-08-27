package com.mco.mchat.ui.signUpFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.mco.mchat.data.model.CreateUser
import com.mco.mchat.data.repo.AuthRepository
import com.mco.mchat.data.repo.DatabaseRepository
import com.mco.mchat.ui.base.BaseViewModel
import com.mco.mchat.data.Result
import com.mco.mchat.data.entity.User
import com.mco.mchat.data.storage.PrefsDataStoreManager
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val dbRepository: DatabaseRepository,
    private val authRepository: AuthRepository,
    val dataStorage: PrefsDataStoreManager
) : BaseViewModel() {

    private var _isCreatedUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val isCreatedUser: LiveData<FirebaseUser> = _isCreatedUser

    fun createAccount(createUser: CreateUser) {
        if (validate(createUser)) {
            viewModelScope.launch {
                mLoading.value = true
                authRepository.createUser(createUser) { result: Result<FirebaseUser> ->
                    onResult(null, result)
                    if (result is Result.Success) {
                        _isCreatedUser.value = result.data!!
                        dbRepository.updateNewUser(User().apply {
                            info.id = result.data.uid
                            info.displayName = createUser.displayName
                        })
                    }
                }
                mLoading.value = false
            }
        }
    }

    private fun validate(createUser: CreateUser): Boolean {
        if (createUser.displayName.isEmpty()) {
            mMessage.value = "Họ và tên trống !"
            return false
        }

        if (createUser.email.isEmpty()) {
            mMessage.value = "Địa chỉ email trống !"
            return false
        }
        if (createUser.password.isEmpty()) {
            mMessage.value = "Mật khẩu trống !"
            return false
        }
        return true
    }
}