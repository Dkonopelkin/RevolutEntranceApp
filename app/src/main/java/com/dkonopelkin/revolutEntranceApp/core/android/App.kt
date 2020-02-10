package com.dkonopelkin.revolutEntranceApp.core.android

import android.app.Application
import com.dkonopelkin.revolutEntranceApp.core.di.AppInjector
import com.dkonopelkin.revolutEntranceApp.core.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDi()
    }

    private fun initDi() {
        AppInjector.appDependencies = DaggerAppComponent.factory().create(this)
    }
}