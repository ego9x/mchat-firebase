package com.mco.mchat.ui.main.chat.chatBox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mco.mchat.data.entity.Message
import com.mco.mchat.databinding.MessageReceiverItemLayoutBinding
import com.mco.mchat.databinding.MessageSenderItemLayoutBinding
import com.mco.mchat.utils.Ext.getDateTimeFromEpocLongOfSeconds
import com.mco.mchat.utils.Ext.toDateTime
import java.lang.Math.abs

class MessagesListAdapter internal constructor(private val userId: String) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    private val holderTypeMessageReceived = 1
    private val holderTypeMessageSent = 2

    /*override fun submitList(list: List<Message>?) {
        super.submitList(ArrayList<Message>(list ?: listOf()))
    }*/

    class ReceivedViewHolder(private val binding: MessageReceiverItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(message: Message, isVisibleTime: Boolean){
                with(binding){
                    tvMessage.text = message.text
                    tvTime.text = message.epochTimeMs.toDateTime()
                    tvTime.isVisible = isVisibleTime
                }
            }
    }

    class SentViewHolder(val binding: MessageSenderItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message, isVisibleTime: Boolean){
            with(binding){
                tvMessage.text = message.text
                tvTime.text = message.epochTimeMs.toDateTime()
                tvTime.isVisible = isVisibleTime
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderID != userId) {
            holderTypeMessageReceived
        } else {
            holderTypeMessageSent
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var isVisible = true
        var message = getItem(position)
        val halfHourInMilli = 1800000
        if (position != 0) {
            val messageBefore = getItem(position - 1)

            if (abs(messageBefore.epochTimeMs - message.epochTimeMs) > halfHourInMilli) {
                isVisible = true
            } else {
                isVisible = false
            }
        }
        when (holder.itemViewType) {
            holderTypeMessageSent -> (holder as SentViewHolder).bind(
                message, isVisible
            )
            holderTypeMessageReceived -> (holder as ReceivedViewHolder).bind(
                message, isVisible
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            holderTypeMessageSent -> {
                val binding = MessageSenderItemLayoutBinding.inflate(layoutInflater, parent, false)
                SentViewHolder(binding)
            }
            holderTypeMessageReceived -> {
                val binding = MessageReceiverItemLayoutBinding.inflate(layoutInflater, parent, false)
                ReceivedViewHolder(binding)
            }
            else -> {
                throw Exception("Error reading holder type")
            }
        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.epochTimeMs == newItem.epochTimeMs
    }
}