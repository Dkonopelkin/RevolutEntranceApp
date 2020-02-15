package com.dkonopelkin.revolutEntranceApp.core.android

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.dkonopelkin.revolutEntranceApp.core.di.AppInjector
import com.dkonopelkin.revolutEntranceApp.core.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDi()
        setAppLifecycleObserver()
    }

    private fun initDi() {
        AppInjector.appDependencies = DaggerAppComponent.factory().create(this)
    }

    private fun setAppLifecycleObserver() {
        ProcessLifecycleOwner.get()
            .lifecycle.addObserver(AppInjector.appDependencies.appLifecycleObserver)
    }
}