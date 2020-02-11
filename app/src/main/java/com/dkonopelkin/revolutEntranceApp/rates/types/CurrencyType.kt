package com.dkonopelkin.revolutEntranceApp.rates.types

import com.dkonopelkin.revolutEntranceApp.R

enum class CurrencyType(
    val currencyCode: String,
    val currencyDescription: String,
    val iconResId: Int
) {
    AUD("AUD", "Australian Dollar", R.drawable.ic_currency_aud),
    BGN("BGN", "Bulgarian Lev", R.drawable.ic_currency_bgn),
    BRL("BRL", "Brazilian Real", R.drawable.ic_currency_brl),
    CAD("CAD", "Canadian Dollar", R.drawable.ic_currency_cad),
    CHF("CHF", "Swiss Franc", R.drawable.ic_currency_chf),
    CNY("CNY", "Chinese Yuan", R.drawable.ic_currency_cny),
    CZK("CZK", "Czech Republic Koruna", R.drawable.ic_currency_czk),
    DKK("DKK", "Danish Krone", R.drawable.ic_currency_dkk),
    GBP("GBP", "British Pound Sterling", R.drawable.ic_currency_gbp),
    HKD("HKD", "Hong Kong Dollar", R.drawable.ic_currency_hkd),
    HRK("HRK", "Croatian Kuna", R.drawable.ic_currency_hrk),
    HUF("HUF", "Hungarian Forint", R.drawable.ic_currency_huf),
    IDR("IDR", "Indonesian Rupiah", R.drawable.ic_currency_idr),
    ILS("ILS", "Israeli New Sheqel", R.drawable.ic_currency_ils),
    INR("INR", "Indian Rupee", R.drawable.ic_currency_inr),
    ISK("ISK", "Icelandic Króna", R.drawable.ic_currency_isk),
    JPY("JPY", "Japanese Yen", R.drawable.ic_currency_jpy),
    KRW("KRW", "South Korean Won", R.drawable.ic_currency_krw),
    MXN("MXN", "Mexican Peso", R.drawable.ic_currency_mxn),
    MYR("MYR", "Malaysian Ringgit", R.drawable.ic_currency_myr),
    NOK("NOK", "Norwegian Krone", R.drawable.ic_currency_nok),
    NZD("NZD", "New Zealand Dollar", R.drawable.ic_currency_nzd),
    PHP("PHP", "Philippine Peso", R.drawable.ic_currency_php),
    PLN("PLN", "Polish Zloty", R.drawable.ic_currency_pln),
    RON("RON", "Romanian Leu", R.drawable.ic_currency_ron),
    RUB("RUB", "Russian Ruble", R.drawable.ic_currency_rub),
    SEK("SEK", "Swedish Krona", R.drawable.ic_currency_sek),
    SGD("SGD", "Singapore Dollar", R.drawable.ic_currency_sgd),
    THB("THB", "Thai Baht", R.drawable.ic_currency_thb),
    TRY("TRY", "Turkish Lira", R.drawable.ic_currency_try),
    USD("USD", "United States Dollar", R.drawable.ic_currency_usd),
    ZAR("ZAR", "South African Rand", R.drawable.ic_currency_zar);

    companion object {
        fun getByCurrencyCode(code: String): CurrencyType {
            return values().firstOrNull { currencyType -> currencyType.currencyCode == code }
                ?: throw IllegalArgumentException("Unknown currency code")
        }
    }
}