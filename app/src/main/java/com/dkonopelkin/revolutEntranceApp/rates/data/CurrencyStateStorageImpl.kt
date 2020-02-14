package com.dkonopelkin.revolutEntranceApp.rates.data

import com.dkonopelkin.revolutEntranceApp.rates.domain.CurrencyStateStorage
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal

class CurrencyStateStorageImpl : CurrencyStateStorage {

    private val stateSubject = BehaviorSubject.create<CurrencyStateStorage.Currency>()

    init {
        update(CurrencyStateStorage.Currency("EUR", BigDecimal(100)))
    }

    override fun observe(): Observable<CurrencyStateStorage.Currency> {
        return stateSubject.toFlowable(BackpressureStrategy.LATEST).toObservable()
    }

    override fun update(data: CurrencyStateStorage.Currency) {
        stateSubject.onNext(data)
    }
}