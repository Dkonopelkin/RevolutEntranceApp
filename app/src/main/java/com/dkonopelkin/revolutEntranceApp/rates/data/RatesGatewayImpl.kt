package com.dkonopelkin.revolutEntranceApp.rates.data

import android.util.Log
import com.dkonopelkin.revolutEntranceApp.rates.domain.RatesGateway
import io.reactivex.Single

class RatesGatewayImpl(private val ratesApiInterface: RatesApiInterface) : RatesGateway {
    override fun loadRates(currencyCode: String): Single<RatesGateway.Result> {
        return ratesApiInterface.getRates(currencyCode)
            .map { apiResult ->
                Log.d("loadRates", "$apiResult")
                RatesGateway.Result(
                    base = apiResult.base,
                    rates = apiResult.rates
                )
            }
    }
}