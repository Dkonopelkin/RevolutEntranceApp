package com.dkonopelkin.revolutEntranceApp.rates.data

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal
import java.util.*

interface RatesApiInterface {

    @GET("latest")
    fun getRates(@Query("base") currencyCode: String): Single<Rates>

    data class Rates(
        @SerializedName("base") val base: String,
        @SerializedName("date") val date: Date,
        @SerializedName("rates") val rates: Map<String, BigDecimal>
    )
}