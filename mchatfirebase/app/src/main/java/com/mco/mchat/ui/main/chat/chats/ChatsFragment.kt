package com.mco.mchat.ui.main.chat.chats

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mco.mchat.R
import com.mco.mchat.data.model.ChatWithUserInfo
import com.mco.mchat.databinding.FragmentChatBinding
import com.mco.mchat.ui.base.BaseFragment
import com.mco.mchat.ui.main.chat.chatBox.ChatBoxFragment.Companion.ARGS_KEY_CHAT_ID
import com.mco.mchat.ui.main.chat.chatBox.ChatBoxFragment.Companion.ARGS_KEY_OTHER_USER_ID
import com.mco.mchat.ui.main.chat.chats.adapter.ChatItem
import com.mco.mchat.utils.animationOptions
import com.mco.mchat.utils.convertTwoUserIDs
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatsFragment : BaseFragment(R.layout.fragment_chat) {

    lateinit var binding: FragmentChatBinding
    private val viewModel: ChatsViewModel by viewModel()

    lateinit var chatAdapter: ItemAdapter<ChatItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)
        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.chatsList.observe(viewLifecycleOwner,{ chats ->
            chatAdapter.set(chats.map { ChatItem(it) })
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            if(it) showLoadingDialog() else hideLoadingDialog()
        })
    }

    private fun setupView() {
        with(binding) {
            chatAdapter = ItemAdapter()
            rcChat.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            val fastAdapter = FastAdapter.with(chatAdapter)

            rcChat.adapter = fastAdapter

            fastAdapter.onClickListener = { _, _, item, _ ->
                navigateToChat(item.chatWithUserInfo)
                false
            }

        }
    }

    private fun navigateToChat(chatWithUserInfo: ChatWithUserInfo) {
        val bundle = bundleOf(
            ARGS_KEY_OTHER_USER_ID to chatWithUserInfo.mUserInfo.id,
            ARGS_KEY_CHAT_ID to convertTwoUserIDs(viewModel.userID, chatWithUserInfo.mUserInfo.id)
        )
        findNavController().navigate(R.id.actionChatToChatBox, bundle,animationOptions)
    }
}