package com.dkonopelkin.revolutEntranceApp.rates.data

import com.dkonopelkin.revolutEntranceApp.rates.domain.LoadRatesGateway
import io.reactivex.Single

class LoadRatesGatewayImpl(private val ratesApiInterface: RatesApiInterface) : LoadRatesGateway {
    override fun loadRates(currencyCode: String): Single<LoadRatesGateway.Result> {
        return ratesApiInterface.getRates(currencyCode)
            .map { apiResult ->
                LoadRatesGateway.Result(
                    base = apiResult.base,
                    rates = apiResult.rates
                )
            }
    }

}