package com.dkonopelkin.revolutEntranceApp.core.di

import com.dkonopelkin.revolutEntranceApp.core.config.ApplicationConst
import com.dkonopelkin.revolutEntranceApp.core.utils.AppLifecycleObserver
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
abstract class AppModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(ApplicationConst.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC

            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideGson(): Gson = Gson()

        @JvmStatic
        @Provides
        @Singleton
        fun provideAppLifecycleObserver(): AppLifecycleObserver = AppLifecycleObserver()
    }
}