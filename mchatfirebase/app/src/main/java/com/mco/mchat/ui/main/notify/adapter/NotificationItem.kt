package com.mco.mchat.ui.main.notify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.mco.mchat.R
import com.mco.mchat.data.entity.User
import com.mco.mchat.data.entity.UserInfo
import com.mco.mchat.data.entity.UserNotification
import com.mco.mchat.databinding.NotifyItemLayoutBinding
import com.mco.mchat.databinding.UserItemLayoutBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class NotificationItem(val userInfo: UserInfo) : AbstractBindingItem<NotifyItemLayoutBinding>() {

    override val type: Int
        get() = R.id.adapter_user_id

    override var identifier: Long
        get() = 0
        set(value) {}

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): NotifyItemLayoutBinding {
        return NotifyItemLayoutBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: NotifyItemLayoutBinding, payloads: List<Any>) {
        with(binding) {
            userInfo.let {
                avatar.load(it.profileImageUrl)
                tvUserName.text = it.displayName
                tvStatus.text = it.status
            }
        }
    }
}