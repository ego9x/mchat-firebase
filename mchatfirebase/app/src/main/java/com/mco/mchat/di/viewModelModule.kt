package com.mco.mchat.di

import com.mco.mchat.ui.base.BaseViewModel
import com.mco.mchat.ui.introduce.IntroViewModel
import com.mco.mchat.ui.main.MainViewModel
import com.mco.mchat.ui.main.chat.chatBox.ChatBoxViewModel
import com.mco.mchat.ui.main.chat.chats.ChatsViewModel
import com.mco.mchat.ui.main.notify.NotifyViewModel
import com.mco.mchat.ui.main.profile.ProfileViewModel
import com.mco.mchat.ui.main.profile.friend.FriendDialogViewModel
import com.mco.mchat.ui.signIn.SignInViewModel
import com.mco.mchat.ui.signUpFragment.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BaseViewModel() }
    viewModel { MainViewModel(get()) }
    viewModel { SignUpViewModel(get(), get(), get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { IntroViewModel() }
    viewModel { ProfileViewModel(get(),get(),get(),get()) }
    viewModel { FriendDialogViewModel(get(),get()) }
    viewModel { NotifyViewModel(get()) }
    viewModel { ChatsViewModel(get()) }
    viewModel { (otherUserID: String,chatID: String) -> ChatBoxViewModel(otherUserID,chatID,get(),get(),get()) }
}