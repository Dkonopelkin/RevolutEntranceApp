package com.dkonopelkin.revolutEntranceApp.rates.di

import com.dkonopelkin.revolutEntranceApp.core.di.AppDependencies
import com.dkonopelkin.revolutEntranceApp.rates.viewmodel.RatesViewModel
import dagger.Component

@Component(modules = [RatesModule::class], dependencies = [AppDependencies::class])
@RatesScope
interface RatesComponent {
    val ratesViewModel: RatesViewModel

    @Component.Factory
    interface Factory {
        fun create(appDependencies: AppDependencies): RatesComponent
    }

}