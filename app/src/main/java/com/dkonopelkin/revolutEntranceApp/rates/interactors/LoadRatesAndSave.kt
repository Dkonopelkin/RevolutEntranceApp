package com.dkonopelkin.revolutEntranceApp.rates.interactors

import android.util.Log
import com.dkonopelkin.revolutEntranceApp.rates.domain.LoadRatesGateway
import com.dkonopelkin.revolutEntranceApp.rates.domain.RatesRepository
import io.reactivex.Completable

class LoadRatesAndSave(
    private val loadRatesGateway: LoadRatesGateway,
    private val ratesRepository: RatesRepository
) {
    fun invoke(currencyCode: String): Completable {
        return loadRatesGateway.loadRates(currencyCode)
            .flatMapCompletable { result ->
                Log.d("repository", "before repository save")
                ratesRepository.save(
                    result.base,
                    result.rates
                )
            }
    }
}