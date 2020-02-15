package com.dkonopelkin.revolutEntranceApp.rates.di

import android.content.Context
import androidx.room.Room
import com.dkonopelkin.revolutEntranceApp.core.utils.AppLifecycleObserver
import com.dkonopelkin.revolutEntranceApp.rates.data.*
import com.dkonopelkin.revolutEntranceApp.rates.domain.*
import com.dkonopelkin.revolutEntranceApp.rates.viewmodel.RatesViewModel
import com.google.gson.Gson
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
            loadRatesAndUpdateRepository: LoadRatesAndUpdateRepository,
            currencyStateStorage: CurrencyStateStorage,
            getUiStateObserver: GetUiStateObserver,
            updateRatesIntervalObserver: UpdateRatesIntervalObserver,
            appLifecycleObserver: AppLifecycleObserver
        ): RatesViewModel {
            return RatesViewModel(
                loadRatesAndUpdateRepository = loadRatesAndUpdateRepository,
                currencyStateStorage = currencyStateStorage,
                getUiStateObserver = getUiStateObserver,
                updateRatesIntervalObserver = updateRatesIntervalObserver,
                appLifecycleObserver = appLifecycleObserver
            )
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideLoadRatesAndUpdateRepository(
            ratesRepository: RatesRepository,
            ratesGateway: RatesGateway
        ): LoadRatesAndUpdateRepository {
            return LoadRatesAndUpdateRepository(
                ratesRepository = ratesRepository,
                ratesGateway = ratesGateway
            )
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideUpdateRatesIntervalObserver(
            currencyStateStorage: CurrencyStateStorage
        ): UpdateRatesIntervalObserver {
            return UpdateRatesIntervalObserver(
                currencyStateStorage = currencyStateStorage
            )
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideGetUiStateObserver(
            ratesRepository: RatesRepository,
            currencyStateStorage: CurrencyStateStorage
        ): GetUiStateObserver {
            return GetUiStateObserver(
                ratesRepository = ratesRepository,
                currencyStateStorage = currencyStateStorage
            )
        }

        @JvmStatic
        @Provides
        @RatesScope
        fun provideCurrencyStateStorage(context: Context, gson: Gson): CurrencyStateStorage =
            CurrencyStateStorageImpl(context, gson)


        @JvmStatic
        @Provides
        @RatesScope
        fun provideLoadRatesGateway(ratesApiInterface: RatesApiInterface): RatesGateway =
            RatesGatewayImpl(ratesApiInterface = ratesApiInterface)

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