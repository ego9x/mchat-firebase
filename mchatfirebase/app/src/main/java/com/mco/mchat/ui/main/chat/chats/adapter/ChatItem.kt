package com.mco.mchat.ui.main.chat.chats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.mco.mchat.R
import com.mco.mchat.data.entity.UserInfo
import com.mco.mchat.data.model.ChatWithUserInfo
import com.mco.mchat.databinding.ChatItemLayoutBinding
import com.mco.mchat.utils.Ext.getDateTimeFromEpocLongOfSeconds
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ChatItem(val chatWithUserInfo: ChatWithUserInfo) : AbstractBindingItem<ChatItemLayoutBinding>() {

    override val type: Int
        get() = R.id.adapter_user_id

    override var identifier: Long
        get() = chatWithUserInfo.mChat.lastMessage.epochTimeMs
        set(value) {}

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ChatItemLayoutBinding {
        return ChatItemLayoutBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ChatItemLayoutBinding, payloads: List<Any>) {
        with(binding) {
            chatWithUserInfo.let {
                avatarView.load(it.mUserInfo.profileImageUrl)
                avatarView.showBadge = it.mUserInfo.online
                tvDisplayName.text = it.mUserInfo.displayName
                tvMessage.text = it.mChat.lastMessage.text
                timeText.text = it.mChat.lastMessage.epochTimeMs.getDateTimeFromEpocLongOfSeconds()
            }
        }
    }
}