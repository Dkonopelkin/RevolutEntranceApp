package com.dkonopelkin.revolutEntranceApp.rates.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.dkonopelkin.revolutEntranceApp.rates.domain.CurrencyStateStorage
import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal

class CurrencyStateStorageImpl(context: Context, private val gson: Gson) : CurrencyStateStorage {

    private val STORAGE_NAME = "CurrencyStateStorage"
    private val DATA_KEY = "CurrencyStateStorageData"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(STORAGE_NAME, MODE_PRIVATE)

    private val stateSubject = BehaviorSubject.create<CurrencyStateStorage.Currency>()

    init {
        val isSavedDataExists = sharedPreferences.contains(DATA_KEY)
        if (isSavedDataExists) {
            val data = gson.fromJson(
                sharedPreferences.getString(DATA_KEY, ""),
                CurrencyStateStorage.Currency::class.java
            )
            update(data)
        } else {
            update(CurrencyStateStorage.Currency("EUR", BigDecimal(100)))
        }
    }

    override fun observe(): Observable<CurrencyStateStorage.Currency> {
        return stateSubject.toFlowable(BackpressureStrategy.LATEST).toObservable()
    }

    override fun update(data: CurrencyStateStorage.Currency) {
        sharedPreferences.edit().putString(DATA_KEY, gson.toJson(data)).apply()
        stateSubject.onNext(data)
    }
}