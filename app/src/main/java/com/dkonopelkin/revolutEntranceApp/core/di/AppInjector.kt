package com.dkonopelkin.revolutEntranceApp.core.di

internal object AppInjector {
    lateinit var appDependencies: AppDependencies
}

val appDependencies: AppDependencies
    get() {
        return AppInjector.appDependencies
    }