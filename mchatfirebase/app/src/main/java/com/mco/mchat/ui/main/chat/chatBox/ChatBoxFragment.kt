package com.mco.mchat.ui.main.chat.chatBox

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.mco.mchat.R
import com.mco.mchat.databinding.FragmentChatBoxBinding
import com.mco.mchat.ui.base.BaseFragment
import com.mco.mchat.ui.main.chat.chatBox.adapter.MessagesListAdapter
import com.mco.mchat.utils.animationOptions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatBoxFragment : BaseFragment(R.layout.fragment_chat_box) {

    lateinit var binding: FragmentChatBoxBinding
    private lateinit var messageAdapter: MessagesListAdapter
    val viewModel: ChatBoxViewModel by viewModel {
        parametersOf(
            requireArguments().getString(ARGS_KEY_OTHER_USER_ID),
            requireArguments().getString(ARGS_KEY_CHAT_ID)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBoxBinding.bind(view)
        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.messagesList.observe(viewLifecycleOwner, {
            messageAdapter.submitList(it)
            binding.edtMessage.text = null
        })

        viewModel.otherUser.observe(viewLifecycleOwner, {
            with(binding){
                tvOtherUser.text = it.displayName
                avatarView.showBadge = it.online
                if(it.profileImageUrl.isNotEmpty())
                    avatarView.load(it.profileImageUrl)
                else
                    avatarView.text = it.displayName
            }
        })
    }

    private fun setupView() {
        with(binding) {
            messageAdapter = MessagesListAdapter(viewModel.userID)

            rcMessage.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            rcMessage.adapter = messageAdapter

            btSend.setOnClickListener {
                edtMessage.text.toString().let {
                    if(it.isNotEmpty()){
                        viewModel.sendMessagePressed(it)
                    }
                }
            }

            imgBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        const val ARGS_KEY_OTHER_USER_ID = "bundle_other_user_id"
        const val ARGS_KEY_CHAT_ID = "bundle_other_chat_id"
    }
}