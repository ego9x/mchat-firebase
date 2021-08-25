package com.mco.mchat.di

import com.mco.mchat.data.repo.AuthRepository
import com.mco.mchat.data.repo.DatabaseRepository
import com.mco.mchat.data.repo.StorageRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepository(get()) }
    single { DatabaseRepository(get()) }
    single { StorageRepository(get()) }
}