package com.dkonopelkin.revolutEntranceApp.rates.domain

import com.dkonopelkin.revolutEntranceApp.core.domain.Storage
import java.math.BigDecimal

interface CurrencyStateStorage : Storage<CurrencyStateStorage.Currency> {
    data class Currency(val code: String, val amount: BigDecimal)
}