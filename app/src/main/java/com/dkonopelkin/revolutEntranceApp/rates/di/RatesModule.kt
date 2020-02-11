package com.dkonopelkin.revolutEntranceApp.rates.di

import com.dkonopelkin.revolutEntranceApp.rates.data.LoadRatesGatewayImpl
import com.dkonopelkin.revolutEntranceApp.rates.data.RatesApiInterface
import com.dkonopelkin.revolutEntranceApp.rates.domain.LoadRatesGateway
import com.dkonopelkin.revolutEntranceApp.rates.presentation.RatesViewModel
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
abstract class RatesModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @RatesScope
        fun provideRatesApiInterface(retrofit: Retrofit): RatesApiInterface {
            return retrofit.create(RatesApiInterface::class.java)
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideRatesViewModel(loadRatesGateway: LoadRatesGateway): RatesViewModel {
            return RatesViewModel(
                loadRatesGateway = loadRatesGateway
            )
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideLoadRatesGateway(ratesApiInterface: RatesApiInterface): LoadRatesGateway {
            return LoadRatesGatewayImpl(ratesApiInterface = ratesApiInterface)
        }

    }
}