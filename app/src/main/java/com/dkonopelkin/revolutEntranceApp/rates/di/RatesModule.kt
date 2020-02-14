package com.dkonopelkin.revolutEntranceApp.rates.di

import android.content.Context
import androidx.room.Room
import com.dkonopelkin.revolutEntranceApp.rates.data.*
import com.dkonopelkin.revolutEntranceApp.rates.domain.CurrencyStateStorage
import com.dkonopelkin.revolutEntranceApp.rates.domain.LoadRatesGateway
import com.dkonopelkin.revolutEntranceApp.rates.domain.RatesRepository
import com.dkonopelkin.revolutEntranceApp.rates.interactors.LoadRatesAndSave
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
        fun provideRatesViewModel(
            loadRatesAndSave: LoadRatesAndSave,
            ratesRepository: RatesRepository,
            currencyStateStorage: CurrencyStateStorage
        ): RatesViewModel {
            return RatesViewModel(
                loadRatesAndSave = loadRatesAndSave,
                ratesRepository = ratesRepository,
                currencyStateStorage = currencyStateStorage
            )
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideLoadRatesAndSave(
            ratesRepository: RatesRepository,
            loadRatesGateway: LoadRatesGateway
        ): LoadRatesAndSave {
            return LoadRatesAndSave(
                ratesRepository = ratesRepository,
                loadRatesGateway = loadRatesGateway
            )
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideCurrencyStateStorage(): CurrencyStateStorage = CurrencyStateStorageImpl()


        @JvmStatic
        @Provides
        @RatesScope
        fun provideLoadRatesGateway(ratesApiInterface: RatesApiInterface): LoadRatesGateway {
            return LoadRatesGatewayImpl(ratesApiInterface = ratesApiInterface)
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideRatesDatabase(context: Context): RatesCacheDatabase {
            return Room.databaseBuilder(
                context,
                RatesCacheDatabase::class.java,
                "rates_cache_database"
            ).build()
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideRatesRepository(ratesCacheDatabase: RatesCacheDatabase): RatesRepository {
            return RatesRepositoryImpl(database = ratesCacheDatabase)
        }

    }
}