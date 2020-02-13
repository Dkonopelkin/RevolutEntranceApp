package com.dkonopelkin.revolutEntranceApp.rates.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyRateEntity::class], version = 1)
abstract class RatesCacheDatabase : RoomDatabase() {
    abstract fun getRatesDao(): RatesDao
}