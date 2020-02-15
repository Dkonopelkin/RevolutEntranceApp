package com.dkonopelkin.revolutEntranceApp.rates.domain

import io.reactivex.Single
import java.math.BigDecimal

interface RatesGateway {
    fun loadRates(currencyCode: String): Single<Result>

    data class Result(
        val base: String,
        val rates: Map<String, BigDecimal>
    )
}