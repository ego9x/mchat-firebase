package com.mco.mchat.ui.main.notify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mco.mchat.R
import com.mco.mchat.databinding.FragmentNotifyBinding
import com.mco.mchat.databinding.NotifyItemLayoutBinding
import com.mco.mchat.ui.base.BaseFragment
import com.mco.mchat.ui.main.notify.adapter.NotificationItem
import com.mco.mchat.ui.main.profile.adapter.UserItem
import com.mco.mchat.utils.asBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotifyFragment : BaseFragment(R.layout.fragment_notify) {

    lateinit var notifyAdapter: ItemAdapter<NotificationItem>
    lateinit var binding: FragmentNotifyBinding
    private val viewModel: NotifyViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotifyBinding.bind(view)
        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.usersInfoList.observe(viewLifecycleOwner, { users ->
            notifyAdapter.set(users.map { NotificationItem(it) })
        })
    }

    private fun setupView() {
        with(binding) {
            notifyAdapter = ItemAdapter()
            rcNotification.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            val fastAdapter = FastAdapter.with(notifyAdapter)

            rcNotification.adapter = fastAdapter

            fastAdapter.addEventHook(object : ClickEventHook<NotificationItem>() {
                override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                    return viewHolder.asBinding<NotifyItemLayoutBinding> {
                        it.btAccept
                    }
                }

                override fun onClick(
                    v: View,
                    position: Int,
                    fastAdapter: FastAdapter<NotificationItem>,
                    item: NotificationItem
                ) {
                    viewModel.acceptNotificationPressed(item.userInfo)
                }
            })

            fastAdapter.addEventHook(object : ClickEventHook<NotificationItem>() {
                override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                    return viewHolder.asBinding<NotifyItemLayoutBinding> {
                        it.btDecline
                    }
                }

                override fun onClick(
                    v: View,
                    position: Int,
                    fastAdapter: FastAdapter<NotificationItem>,
                    item: NotificationItem
                ) {
                    viewModel.declineNotificationPressed(item.userInfo)
                }
            })

        }
    }

}