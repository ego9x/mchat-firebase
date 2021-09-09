package com.mco.mchat.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class UserItem {
}

/*class PeopleItem(val user: User): AbstractBindingItem<PeopleItemLayoutBinding>() {


    override val type: Int
        get() = R.id.fastadapter_people_item_id

    override var identifier: Long
        get() = super.identifier
        set(value) {user.id}

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): PeopleItemLayoutBinding {
        return PeopleItemLayoutBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: PeopleItemLayoutBinding, payloads: List<Any>) {
        with(binding){
            (user.firstName +" "+ user.lastName).also { tvUserName.text = it }
            with(user){
                val firstCharacter = firstName?.get(0) ?:""
                val secondCharacter = lastName?.get(0) ?:""
                (firstCharacter.toString() + secondCharacter.toString()).also { imgAvatar.text = it }
            }
        }
    }

}*/
