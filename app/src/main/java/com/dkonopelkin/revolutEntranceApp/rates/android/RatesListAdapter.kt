package com.dkonopelkin.revolutEntranceApp.rates.android

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dkonopelkin.revolutEntranceApp.R
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
        items = newItems
        notifyDataSetChanged()
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

    class RatesListViewHolder(override val containerView: View, val callback: Callback) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: RatesViewModel.UIState.RateItem) {
            titleTextView.text = item.code
            descriptionTextView.text = item.description
            iconImageView.setImageDrawable(item.icon)
            inputField.setText(item.amount.toString())

            containerView.setOnClickListener { inputField.requestFocus() }
            inputField.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    callback.onItemSelected(item.code)
                }
            }
            inputField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(newValue: Editable) {
                    callback.onValueChanged(item.code, newValue.toString())
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
    }

    interface Callback {
        fun onItemSelected(currencyCode: String)
        fun onValueChanged(currencyCode: String, newValue: String)
    }
}