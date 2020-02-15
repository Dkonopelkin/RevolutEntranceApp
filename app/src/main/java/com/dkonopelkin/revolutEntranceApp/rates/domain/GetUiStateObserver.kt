package com.dkonopelkin.revolutEntranceApp.rates.domain

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.math.BigDecimal

class GetUiStateObserver(
    private val ratesRepository: RatesRepository,
    private val currencyStateStorage: CurrencyStateStorage
) {
    fun invoke(): Observable<List<Currency>> {
        return currencyStateStorage.observe().switchMap { baseCurrency ->
            Observable.combineLatest(
                Observable.just(baseCurrency),
                ratesRepository.observeRatesByCode(baseCurrency.code),
                BiFunction<CurrencyStateStorage.Currency, Map<String, BigDecimal>, List<Currency>> { currency, rates ->
                    // TODO .scan or .collect
                    val mutableList = mutableListOf<Currency>()
                    rates.keys.forEach { code ->
                        val amountRatio = rates[code]!! * baseCurrency.amount
                        mutableList.add(Currency(code, amountRatio))
                    }
                    mutableList.add(0, Currency(currency.code, currency.amount))
                    mutableList
                })
        }
    }

    data class Currency(val code: String, val amount: BigDecimal)
}