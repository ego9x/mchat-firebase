package com.mco.mchat.ui.main.profile.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mco.mchat.data.Result
import com.mco.mchat.data.entity.*
import com.mco.mchat.data.repo.DatabaseRepository
import com.mco.mchat.data.storage.PrefsDataStoreManager
import com.mco.mchat.firebase.FirebaseReferenceValueObserver
import com.mco.mchat.ui.base.BaseViewModel
import com.mco.mchat.utils.convertTwoUserIDs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FriendDialogViewModel(
    private val dbRepository: DatabaseRepository,
    private val firebaseReferenceObserver: FirebaseReferenceValueObserver
) : BaseViewModel() {

    private val _myUser: MutableLiveData<User> = MutableLiveData()
    private val _otherUser: MutableLiveData<User> = MutableLiveData()
    val otherUser: LiveData<User> = _otherUser

    val layoutState = MediatorLiveData<LayoutState>()

    init{
        layoutState.addSource(_myUser) { updateLayoutState(it, _otherUser.value) }
    }

    private fun updateLayoutState(myUser: User?, otherUser: User?) {
        if (myUser != null && otherUser != null) {
            layoutState.value = when {
                myUser.friends[otherUser.info.id] != null -> LayoutState.IS_FRIEND
                myUser.sentRequests[otherUser.info.id] != null -> LayoutState.REQUEST_SENT
                else -> LayoutState.NOT_FRIEND
            }
        }
    }

    fun setupProfile(userId: String) {
        dbRepository.loadUser(userId) { result: Result<User> ->
            onResult(_otherUser, result)
            if (result is Result.Success) {
                dbRepository.loadAndObserveUser(userID, firebaseReferenceObserver) { result2: Result<User> ->
                    onResult(_myUser, result2)
                }
            }
        }
    }

    fun addFriendPressed() {
        dbRepository.updateNewSentRequest(userID, UserRequest(_otherUser.value!!.info.id))
        dbRepository.updateNewNotification(_otherUser.value!!.info.id, UserNotification(userID))
    }

    fun removeFriendPressed() {
        dbRepository.removeFriend(userID, _otherUser.value!!.info.id)
        dbRepository.removeChat(convertTwoUserIDs(userID, _otherUser.value!!.info.id))
        dbRepository.removeMessages(convertTwoUserIDs(userID, _otherUser.value!!.info.id))
    }

    fun acceptFriendRequestPressed() {
        dbRepository.updateNewFriend(UserFriend(userID), UserFriend(_otherUser.value!!.info.id))

        val newChat = Chat().apply {
            info.id = convertTwoUserIDs(userID, _otherUser.value!!.info.id)
            lastMessage = Message(seen = true, text = "Say hello!")
        }

        dbRepository.updateNewChat(newChat)
        dbRepository.removeNotification(userID, _otherUser.value!!.info.id)
        dbRepository.removeSentRequest(_otherUser.value!!.info.id, userID)
    }

    fun declineFriendRequestPressed() {
        dbRepository.removeSentRequest(userID, _otherUser.value!!.info.id)
        dbRepository.removeNotification(userID, _otherUser.value!!.info.id)
    }
}

enum class LayoutState {
    IS_FRIEND, NOT_FRIEND, ACCEPT_DECLINE, REQUEST_SENT
}