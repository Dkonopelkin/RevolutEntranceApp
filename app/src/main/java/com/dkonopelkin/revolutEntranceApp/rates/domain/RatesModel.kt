package com.dkonopelkin.revolutEntranceApp.rates.domain

import java.math.BigDecimal

data class RatesModel(
    val baseCurrencyCode: String,
    val rates: Map<String, BigDecimal>
)