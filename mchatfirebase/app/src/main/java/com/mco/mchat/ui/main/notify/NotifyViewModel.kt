package com.mco.mchat.ui.main.notify

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mco.mchat.data.repo.DatabaseRepository
import com.mco.mchat.ui.base.BaseViewModel
import com.mco.mchat.utils.Ext.addNewItem
import com.mco.mchat.data.Result
import com.mco.mchat.data.entity.*
import com.mco.mchat.utils.Ext.removeItem
import com.mco.mchat.utils.convertTwoUserIDs

class NotifyViewModel(private val dbRepository: DatabaseRepository): BaseViewModel() {

    private val updatedUserInfo = MutableLiveData<UserInfo>()
    private val userNotificationsList = MutableLiveData<MutableList<UserNotification>>()

    val usersInfoList = MediatorLiveData<MutableList<UserInfo>>()

    init {

        usersInfoList.addSource(updatedUserInfo) { usersInfoList.addNewItem(it) }
        loadNotifications()
    }

    private fun loadNotifications() {
        dbRepository.loadNotifications(userID) { result: Result<MutableList<UserNotification>> ->
            onResult(userNotificationsList, result)
            if (result is Result.Success) result.data?.forEach { loadUserInfo(it) }
        }
    }

    private fun loadUserInfo(userNotification: UserNotification) {
        dbRepository.loadUserInfo(userNotification.userID) { result: Result<UserInfo> ->
            onResult(updatedUserInfo, result)
        }
    }

    private fun updateNotification(otherUserInfo: UserInfo, removeOnly: Boolean) {
        val userNotification = userNotificationsList.value?.find {
            it.userID == otherUserInfo.id
        }

        if (userNotification != null) {
            if (!removeOnly) {
                dbRepository.updateNewFriend(UserFriend(userID), UserFriend(otherUserInfo.id))
                val newChat = Chat().apply {
                    info.id = convertTwoUserIDs(userID, otherUserInfo.id)
                    lastMessage = Message(seen = true, text = "Say hello!")
                }
                dbRepository.updateNewChat(newChat)
            }
            dbRepository.removeNotification(userID, otherUserInfo.id)
            dbRepository.removeSentRequest(otherUserInfo.id, userID)

            usersInfoList.removeItem(otherUserInfo)
            userNotificationsList.removeItem(userNotification)
        }
    }

    fun acceptNotificationPressed(userInfo: UserInfo) {
        updateNotification(userInfo, false)
    }

    fun declineNotificationPressed(userInfo: UserInfo) {
        updateNotification(userInfo, true)
    }

}