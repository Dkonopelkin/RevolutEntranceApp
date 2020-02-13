package com.dkonopelkin.revolutEntranceApp.rates.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "rates_table", primaryKeys = ["base_currency_code", "currency_code"])
data class CurrencyRateEntity(
    @ColumnInfo(name = "base_currency_code") val baseCurrencyCode: String,
    @ColumnInfo(name = "currency_code") val currencyCode: String,
    @ColumnInfo(name = "ratio") val ratio: String
)