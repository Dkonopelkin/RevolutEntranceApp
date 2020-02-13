package com.dkonopelkin.revolutEntranceApp.rates.domain

import io.reactivex.Completable
import io.reactivex.Observable
import java.math.BigDecimal

interface RatesRepository {
    fun save(baseCurrencyCode: String, rates: Map<String, BigDecimal>): Completable
    fun observeRatesByCode(baseCode: String): Observable<Map<String, BigDecimal>>
    fun clear()
}