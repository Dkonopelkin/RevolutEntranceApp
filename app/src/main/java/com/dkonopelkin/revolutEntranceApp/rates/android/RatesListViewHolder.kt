package com.dkonopelkin.revolutEntranceApp.rates.android

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dkonopelkin.revolutEntranceApp.core.di.AppInjector
import com.dkonopelkin.revolutEntranceApp.core.utils.hideKeyboard
import com.dkonopelkin.revolutEntranceApp.core.utils.update
import com.dkonopelkin.revolutEntranceApp.rates.viewmodel.RatesViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_rates.*

class RatesListViewHolder(
    override val containerView: View,
    val callback: RatesListAdapter.Callback
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RatesViewModel.UIState.RateItem) {
        bindCurrencyInfo(item)

        if (!inputField.hasFocus()) {
            inputField.update(item.amount)
            inputField.filters = arrayOf()
        } else {
            inputField.filters = arrayOf(InputFilter.LengthFilter(6))
        }

        containerView.setOnClickListener { inputField.requestFocus() }
        inputField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                callback.onItemSelected(item.code, item.amount)
            } else {
                inputField.hideKeyboard()
            }
        }
        inputField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(newValue: Editable) {
                if (inputField.hasFocus() && item.isBaseCurrency) {
                    callback.onValueChanged(item.code, newValue.toString())
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun bindCurrencyInfo(item: RatesViewModel.UIState.RateItem) {
        titleTextView.text = item.code
        descriptionTextView.text = item.description
        iconImageView.setImageDrawable(
            ContextCompat.getDrawable(AppInjector.appDependencies.context, item.icon)!!
        )
    }
}