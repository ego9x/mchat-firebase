package com.mco.mchat.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single { provideRetrofit(get()) }
    single { provideOkHttpClient() }
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