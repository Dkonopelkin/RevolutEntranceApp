package com.dkonopelkin.revolutEntranceApp.rates.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dkonopelkin.revolutEntranceApp.core.di.AppInjector.appDependencies

class RatesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DaggerRatesComponent.factory().create(appDependencies = appDependencies).ratesViewModel as T
    }
}