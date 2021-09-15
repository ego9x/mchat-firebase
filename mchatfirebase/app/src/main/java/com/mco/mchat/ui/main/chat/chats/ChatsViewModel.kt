package com.mco.mchat.ui.main.chat.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mco.mchat.data.model.ChatWithUserInfo
import com.mco.mchat.data.Result
import com.mco.mchat.data.entity.Chat
import com.mco.mchat.data.entity.UserFriend
import com.mco.mchat.data.entity.UserInfo
import com.mco.mchat.data.repo.DatabaseRepository
import com.mco.mchat.firebase.FirebaseReferenceValueObserver
import com.mco.mchat.ui.base.BaseViewModel
import com.mco.mchat.utils.Ext.addNewItem
import com.mco.mchat.utils.Ext.updateItemAt
import com.mco.mchat.utils.convertTwoUserIDs
import kotlinx.coroutines.launch

class ChatsViewModel(private val repository: DatabaseRepository): BaseViewModel() {

    private val firebaseReferenceObserverList = ArrayList<FirebaseReferenceValueObserver>()
    private val _updatedChatWithUserInfo = MutableLiveData<ChatWithUserInfo>()
    private val _selectedChat = MutableLiveData<ChatWithUserInfo>()

    var selectedChat: LiveData<ChatWithUserInfo> = _selectedChat
    val chatsList = MediatorLiveData<MutableList<ChatWithUserInfo>>()

    init {
        chatsList.addSource(_updatedChatWithUserInfo) { newChat ->
            val chat = chatsList.value?.find { it.mChat.info.id == newChat.mChat.info.id }
            if (chat == null) {
                chatsList.addNewItem(newChat)
            } else {
                chatsList.updateItemAt(newChat, chatsList.value!!.indexOf(chat))
            }
        }
        setupChats()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserverList.forEach { it.clear() }
    }

    private fun setupChats() {
        loadFriends()
    }

    private fun loadFriends() {
        repository.loadFriends(userID) { result: Result<List<UserFriend>> ->
            onResult(null, result)
            if (result is Result.Success) result.data?.forEach { loadUserInfo(it) }
        }
    }

    private fun loadUserInfo(userFriend: UserFriend) {
        repository.loadUserInfo(userFriend.userID) { result: Result<UserInfo> ->
            onResult(null, result)
            if (result is Result.Success) result.data?.let { loadAndObserveChat(it) }
        }
    }

    private fun loadAndObserveChat(userInfo: UserInfo) {
        viewModelScope.launch {
            val observer = FirebaseReferenceValueObserver()
            firebaseReferenceObserverList.add(observer)
            repository.loadAndObserveChat(convertTwoUserIDs(userID, userInfo.id), observer) { result: Result<Chat> ->
                onResult(null, result)
                if (result is Result.Success) {
                    _updatedChatWithUserInfo.value = result.data?.let { ChatWithUserInfo(it, userInfo) }
                } else if (result is Result.Error) {
                    chatsList.value?.let {
                        val newList = mutableListOf<ChatWithUserInfo>().apply { addAll(it) }
                        newList.removeIf { it2 -> result.msg.toString().contains(it2.mUserInfo.id) }
                        chatsList.value = newList
                    }
                }
            }
        }
    }

    fun selectChatWithUserInfoPressed(chat: ChatWithUserInfo) {
        _selectedChat.value = chat
    }
}