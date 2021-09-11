package com.mco.mchat.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.mco.mchat.data.entity.UserNotification
import com.mco.mchat.data.repo.AuthRepository
import com.mco.mchat.data.Result
import com.mco.mchat.data.repo.DatabaseRepository
import com.mco.mchat.firebase.FirebaseAuthStateObserver
import com.mco.mchat.firebase.FirebaseDataSource
import com.mco.mchat.firebase.FirebaseReferenceConnectedObserver
import com.mco.mchat.firebase.FirebaseReferenceValueObserver
import com.mco.mchat.ui.base.BaseViewModel

class MainViewModel(
    val firebaseDatabaseService: FirebaseDataSource,
    private val dbRepository: DatabaseRepository,
    private val authRepository: AuthRepository,
    private val fbRefNotificationsObserver: FirebaseReferenceValueObserver,
    private val fbAuthStateObserver: FirebaseAuthStateObserver,
    private val fbRefConnectedObserver: FirebaseReferenceConnectedObserver
) : BaseViewModel() {
    private val _userNotificationsList = MutableLiveData<MutableList<UserNotification>?>()

    var userNotificationsList: LiveData<MutableList<UserNotification>?> = _userNotificationsList

    init {
        setupAuthObserver()
    }

    override fun onCleared() {
        super.onCleared()
        fbRefNotificationsObserver.clear()
        fbRefConnectedObserver.clear()
        fbAuthStateObserver.clear()
    }

    private fun setupAuthObserver(){
        authRepository.observeAuthState(fbAuthStateObserver) { result: Result<FirebaseUser> ->
            if (result is Result.Success) {
                userID = result.data!!.uid
                startObservingNotifications()
                fbRefConnectedObserver.start(userID)
            } else {
                fbRefConnectedObserver.clear()
                stopObservingNotifications()
            }
        }
    }

    private fun startObservingNotifications() {
        dbRepository.loadAndObserveUserNotifications(
            userID,
            fbRefNotificationsObserver
        ) { result: Result<MutableList<UserNotification>> ->
            if (result is Result.Success) {
                _userNotificationsList.value = result.data
            }
        }
    }

    private fun stopObservingNotifications() {
        fbRefNotificationsObserver.clear()
    }
}