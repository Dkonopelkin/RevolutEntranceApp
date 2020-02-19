package com.dkonopelkin.revolutEntranceApp.core.utils

import com.dkonopelkin.revolutEntranceApp.core.utils.TypeConvertExtensions.decimalFormat
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object TypeConvertExtensions {
    private val decimalFormatSymbols = DecimalFormatSymbols()
    val decimalFormat = DecimalFormat()

    init {
        decimalFormatSymbols.decimalSeparator = '.'
        decimalFormat.decimalFormatSymbols = decimalFormatSymbols
        decimalFormat.maximumFractionDigits = 2
        decimalFormat.minimumFractionDigits = 0
        decimalFormat.isGroupingUsed = false
    }
}

fun BigDecimal.toFormattedString(): String {
    if (this.toDouble() == 0.0) {
        return ""
    }
    this.setScale(2, BigDecimal.ROUND_HALF_UP)
    return decimalFormat.format(this)
}

fun String.parseBigDecimal(): BigDecimal {
    val newString = this.replace(oldChar = ',', newChar = '.', ignoreCase = true)
    if (newString.isBlank() || newString == ".") return BigDecimal.ZERO
    return newString.toBigDecimal()
}