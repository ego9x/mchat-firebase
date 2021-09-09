package com.mco.mchat.ui.main.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mco.mchat.data.entity.UserInfo
import com.mco.mchat.data.repo.AuthRepository
import com.mco.mchat.data.repo.DatabaseRepository
import com.mco.mchat.data.repo.StorageRepository
import com.mco.mchat.data.storage.PrefsDataStoreManager
import com.mco.mchat.firebase.FirebaseReferenceValueObserver
import com.mco.mchat.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.mco.mchat.data.Result
import com.mco.mchat.data.entity.User
import com.mco.mchat.ui.main.profile.adapter.UserItem

class ProfileViewModel(
    private val dbRepository: DatabaseRepository,
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository,
    val prefsDataStoreManager: PrefsDataStoreManager,
    private val firebaseReferenceObserver: FirebaseReferenceValueObserver
) : BaseViewModel() {

    lateinit var userID: String

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val friendResponses = MutableLiveData<MutableList<User>>()
    val friends = MediatorLiveData<List<UserItem>>()

    init {
        viewModelScope.launch {
            prefsDataStoreManager.userID.collect { userId ->
                userID = userId
            }
        }

        loadAndObserveUserInfo(userID)
        friends.addSource(friendResponses) { _ ->
            friends.value = friendResponses.value?.filter { it.info.id != userID }?.map {
                UserItem(it)
            }
        }
        loadUsers()

    }

    private fun loadAndObserveUserInfo(userID: String) {
        dbRepository.loadAndObserveUserInfo(userID, firebaseReferenceObserver) { result: Result<UserInfo> ->
            onResult(_userInfo,result)
        }
    }

    fun changeUserStatus(status: String) {
        dbRepository.updateUserStatus(userID, status)
    }

    fun changeUserImage(byteArray: ByteArray) {
        storageRepository.updateUserProfileImage(userID, byteArray) { result: Result<Uri> ->
            onResult(null, result)
            if (result is Result.Success) {
                dbRepository.updateUserProfileImageUrl(userID, result.data.toString())
            }
        }
    }

    private fun loadUsers() {
        dbRepository.loadUsers { result: Result<MutableList<User>> ->
            onResult(friendResponses, result)
        }
    }

    fun logout(){
        viewModelScope.launch {
            prefsDataStoreManager.setUserID("")
            authRepository.logoutUser()
        }
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }
}