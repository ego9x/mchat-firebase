package com.mco.mchat.ui.main.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.mco.mchat.R
import com.mco.mchat.data.entity.User
import com.mco.mchat.databinding.UserItemLayoutBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import java.util.*

class UserItem(val user: User) : AbstractBindingItem<UserItemLayoutBinding>() {

    override val type: Int
        get() = R.id.adapter_user_id

    override var identifier: Long
        get() = 0
        set(value) {}

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserItemLayoutBinding {
        return UserItemLayoutBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: UserItemLayoutBinding, payloads: List<Any>) {
        with(binding) {
            user.info.let {
                avatar.load(it.profileImageUrl)
                tvUserName.text = it.displayName
            }
        }
    }
}