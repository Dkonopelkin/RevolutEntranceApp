package com.dkonopelkin.revolutEntranceApp.core.di

import android.content.Context
import retrofit2.Retrofit

interface AppDependencies {
    val retrofit: Retrofit
    val context: Context
}