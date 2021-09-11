package com.mco.mchat.ui.main.chat.chatBox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mco.mchat.data.entity.Message
import com.mco.mchat.data.entity.UserInfo
import com.mco.mchat.data.repo.DatabaseRepository
import com.mco.mchat.firebase.FirebaseReferenceChildObserver
import com.mco.mchat.firebase.FirebaseReferenceValueObserver
import com.mco.mchat.ui.base.BaseViewModel
import com.mco.mchat.utils.Ext.addNewItem
import com.mco.mchat.data.Result
import com.mco.mchat.data.entity.Chat

class ChatBoxViewModel(
    private val otherUserID: String,
    private val chatID: String,
    private val dbRepository: DatabaseRepository,
    private val fbRefMessagesChildObserver: FirebaseReferenceChildObserver,
    private val fbRefUserInfoObserver: FirebaseReferenceValueObserver
): BaseViewModel() {

    private val _otherUser: MutableLiveData<UserInfo> = MutableLiveData()
    private val _addedMessage = MutableLiveData<Message>()
    val messagesList = MediatorLiveData<MutableList<Message>>()
    val otherUser: LiveData<UserInfo> = _otherUser

    init {
        if(otherUserID.isNotEmpty() || chatID.isNotEmpty()){
            setupChat()
            checkAndUpdateLastMessageSeen()
        }
    }

    override fun onCleared() {
        super.onCleared()
        fbRefMessagesChildObserver.clear()
        fbRefUserInfoObserver.clear()
    }

    private fun checkAndUpdateLastMessageSeen() {
        dbRepository.loadChat(chatID) { result: Result<Chat> ->
            if (result is Result.Success && result.data != null) {
                result.data.lastMessage.let {
                    if (!it.seen && it.senderID != userID) {
                        it.seen = true
                        dbRepository.updateChatLastMessage(chatID, it)
                    }
                }
            }
        }
    }

    private fun setupChat() {
        dbRepository.loadAndObserveUserInfo(otherUserID, fbRefUserInfoObserver) { result: Result<UserInfo> ->
            onResult(_otherUser, result)
            if (result is Result.Success && !fbRefMessagesChildObserver.isObserving()) {
                loadAndObserveNewMessages(chatID)
            }
        }
    }

    private fun loadAndObserveNewMessages(chatID: String) {
        messagesList.addSource(_addedMessage) { messagesList.addNewItem(it) }

        dbRepository.loadAndObserveMessagesAdded(
            chatID,
            fbRefMessagesChildObserver
        ) { result: Result<Message> ->
            onResult(_addedMessage, result)
        }
    }

    fun sendMessagePressed(newMessageText: String) {
        if (newMessageText.isNotBlank()) {
            val newMsg = Message(userID, newMessageText)
            dbRepository.updateNewMessage(chatID, newMsg)
            dbRepository.updateChatLastMessage(chatID, newMsg)
        }
    }
}