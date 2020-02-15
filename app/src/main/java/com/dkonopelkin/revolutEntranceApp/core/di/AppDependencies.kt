package com.dkonopelkin.revolutEntranceApp.core.di

import android.content.Context
import com.dkonopelkin.revolutEntranceApp.core.utils.AppLifecycleObserver
import com.google.gson.Gson
import retrofit2.Retrofit

interface AppDependencies {
    val retrofit: Retrofit
    val context: Context
    val gson: Gson
    val appLifecycleObserver: AppLifecycleObserver
}