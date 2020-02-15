package com.dkonopelkin.revolutEntranceApp.rates.domain

import io.reactivex.Observable
import java.math.BigDecimal

class UiStateObserver(
    private val ratesRepository: RatesRepository,
    private val currencyStateStorage: CurrencyStateStorage
) {
    fun getObservable(): Observable<List<Currency>> {
        return currencyStateStorage.observe().switchMap { baseCurrency ->
            ratesRepository.observeRatesByCode(baseCurrency.code)
                .map { rates -> rates.toList() }
                .map { list ->
                    list.map { (code, amount) ->
                        Currency(
                            code = code,
                            amount = amount * baseCurrency.amount,
                            isCurrent = false
                        )
                    }.toMutableList()
                }
                .map { list ->
                    list.add(0, Currency(baseCurrency.code, baseCurrency.amount, true))
                    list
                }
        }
    }

    data class Currency(val code: String, val amount: BigDecimal, val isCurrent: Boolean)
}