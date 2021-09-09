package com.mco.mchat.di

import com.mco.mchat.ui.introduce.IntroViewModel
import com.mco.mchat.ui.main.MainViewModel
import com.mco.mchat.ui.main.profile.ProfileViewModel
import com.mco.mchat.ui.signIn.SignInViewModel
import com.mco.mchat.ui.signUpFragment.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { SignUpViewModel(get(), get(), get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { IntroViewModel(get()) }
    viewModel { ProfileViewModel(get(),get(),get(),get(),get()) }
}