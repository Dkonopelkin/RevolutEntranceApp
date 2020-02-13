package com.dkonopelkin.revolutEntranceApp.core.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.ViewCompat

fun EditText.update(newValue: String) {
    if (newValue != this.text.toString()) {
        this.setText(newValue)
    }
}

fun View.hideKeyboard() {
    if (ViewCompat.isAttachedToWindow(this)) {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}