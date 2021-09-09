package com.mco.mchat.di

import com.google.firebase.ktx.Firebase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.storage.ktx.storage
import com.google.firebase.database.ktx.database
import com.mco.mchat.data.storage.PrefsDataStoreManager
import com.mco.mchat.firebase.FirebaseAuthSource
import com.mco.mchat.firebase.FirebaseDataSource
import com.mco.mchat.firebase.FirebaseReferenceValueObserver
import com.mco.mchat.firebase.FirebaseStorageSource
import org.koin.android.ext.koin.androidApplication

val appModule = module {
    single { provideRetrofit(get()) }
    single { provideOkHttpClient() }
    single { Firebase.auth }
    single { Firebase.storage }
    single { Firebase.database("https://mchat-97ea4-default-rtdb.asia-southeast1.firebasedatabase.app/") }
    single { FirebaseDataSource() }
    single { FirebaseReferenceValueObserver() }
    single { FirebaseAuthSource() }
    single { FirebaseStorageSource() }
    single { PrefsDataStoreManager(androidApplication().applicationContext) }
}

fun provideOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = if (BuildConfig.DEBUG)
        HttpLoggingInterceptor.Level.BODY
    else
        HttpLoggingInterceptor.Level.NONE
    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
}


fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()
}

private const val BASE_URL = "http://"