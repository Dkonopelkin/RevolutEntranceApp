package com.dkonopelkin.revolutEntranceApp.core.utils

import com.dkonopelkin.revolutEntranceApp.core.utils.TypeConvertExtensions.decimalFormat
import java.math.BigDecimal
import java.text.DecimalFormat

object TypeConvertExtensions {
    val decimalFormat = DecimalFormat()

    init {
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
    if (this.isBlank() || this == ".") return BigDecimal.ZERO
    return newString.toBigDecimal()
}