package com.dkonopelkin.revolutEntranceApp.rates.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface RatesDao {

    @Query("select * from rates_table where base_currency_code =:baseCurrencyCode")
    fun getAllRatioByBaseCode(baseCurrencyCode: String): Observable<List<CurrencyRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencyRateEntity: CurrencyRateEntity)

    @Query("delete from rates_table")
    fun clearAll()
}