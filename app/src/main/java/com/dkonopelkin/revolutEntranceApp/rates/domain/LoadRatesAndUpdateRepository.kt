package com.dkonopelkin.revolutEntranceApp.rates.domain

import io.reactivex.Completable

class LoadRatesAndUpdateRepository(
    private val ratesGateway: RatesGateway,
    private val ratesRepository: RatesRepository
) {
    fun invoke(currencyCode: String): Completable {
        return ratesGateway.loadRates(currencyCode)
            .flatMapCompletable { result ->
                ratesRepository.save(
                    result.base,
                    result.rates
                )
            }
    }
}