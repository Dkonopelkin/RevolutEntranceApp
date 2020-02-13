package com.dkonopelkin.revolutEntranceApp.rates.data

import com.dkonopelkin.revolutEntranceApp.core.utils.parseBigDecimal
import com.dkonopelkin.revolutEntranceApp.rates.domain.RatesRepository
import io.reactivex.Completable
import io.reactivex.Observable
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

class RatesRepositoryImpl(val database: RatesCacheDatabase) : RatesRepository {

    override fun save(baseCurrencyCode: String, rates: Map<String, BigDecimal>): Completable {
        val runnable = Runnable {
            rates.keys.forEach { currencyCode ->
                database.getRatesDao().insert(
                    CurrencyRateEntity(
                        baseCurrencyCode = baseCurrencyCode,
                        currencyCode = currencyCode,
                        ratio = rates[currencyCode].toString()
                    )
                )
            }
        }
        return Completable.fromRunnable(runnable)
    }

    override fun observeRatesByCode(baseCode: String): Observable<Map<String, BigDecimal>> {
        return database.getRatesDao().getAllRatioByBaseCode(baseCode)
            .debounce(50, TimeUnit.MILLISECONDS)
            .map { list ->
                list.map { currencyRateEntity -> currencyRateEntity.currencyCode to currencyRateEntity.ratio.parseBigDecimal() }
                    .toMap()
            }
    }

    override fun clear() {
        database.getRatesDao().clearAll()
    }
}