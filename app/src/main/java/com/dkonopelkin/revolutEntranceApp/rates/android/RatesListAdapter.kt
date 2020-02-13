package com.dkonopelkin.revolutEntranceApp.rates.android

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dkonopelkin.revolutEntranceApp.R
import com.dkonopelkin.revolutEntranceApp.core.di.AppInjector
import com.dkonopelkin.revolutEntranceApp.core.utils.hideKeyboard
import com.dkonopelkin.revolutEntranceApp.core.utils.update
import com.dkonopelkin.revolutEntranceApp.rates.presentation.RatesViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_rates.*


class RatesListAdapter(
    initialItems: List<RatesViewModel.UIState.RateItem>,
    private val context: Context,
    private val callback: Callback
) :
    RecyclerView.Adapter<RatesListAdapter.RatesListViewHolder>() {

    private var items = initialItems

    fun updateItems(newItems: List<RatesViewModel.UIState.RateItem>) {
        val diffCallback = RatesDiffUtilCallback(items, newItems)
        items = newItems
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rates, parent, false)
        return RatesListViewHolder(
            containerView = view,
            callback = callback
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RatesListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(
        holder: RatesListViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.bind(items[position])
    }

    class RatesListViewHolder(override val containerView: View, val callback: Callback) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

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
                ContextCompat.getDrawable(
                    AppInjector.appDependencies.context,
                    item.icon
                )!!
            )
        }
    }

    interface Callback {
        fun onItemSelected(currencyCode: String, amount: String)
        fun onValueChanged(currencyCode: String, amount: String)
    }

    class RatesDiffUtilCallback(
        private val oldList: List<RatesViewModel.UIState.RateItem>,
        private val newList: List<RatesViewModel.UIState.RateItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return Unit
        }
    }

}