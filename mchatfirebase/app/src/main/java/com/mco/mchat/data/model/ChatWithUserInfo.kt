package com.mco.mchat.data.model

import com.mco.mchat.data.entity.Chat
import com.mco.mchat.data.entity.UserInfo

data class ChatWithUserInfo(
    var mChat: Chat,
    var mUserInfo: UserInfo
)