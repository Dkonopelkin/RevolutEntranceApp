package com.dkonopelkin.revolutEntranceApp.rates.domain

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class UpdateRatesIntervalObserver(
    private val currencyStateStorage: CurrencyStateStorage
) {
    private val intervalUpdateSignal = Observable.interval(5, TimeUnit.SECONDS).startWith(0)

    fun getObservable(): Observable<String> {
        return Observable
            .combineLatest<Long, CurrencyStateStorage.Currency, String>(
                intervalUpdateSignal,
                currencyStateStorage.observe().observeOn(Schedulers.io()),
                BiFunction { _, currency -> currency.code }
            )
    }
}