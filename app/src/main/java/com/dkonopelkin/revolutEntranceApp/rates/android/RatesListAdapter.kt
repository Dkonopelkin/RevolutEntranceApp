package com.dkonopelkin.revolutEntranceApp.rates.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dkonopelkin.revolutEntranceApp.R
import com.dkonopelkin.revolutEntranceApp.rates.presentation.RatesViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_rates.*

class RatesListAdapter(initialItems: List<RatesViewModel.UIState.RateItem>, val context: Context) :
    RecyclerView.Adapter<RatesListAdapter.RatesListViewHolder>() {

    private var items = initialItems

    fun updateItems(newItems: List<RatesViewModel.UIState.RateItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesListViewHolder {
        return RatesListViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_rates,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RatesListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class RatesListViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: RatesViewModel.UIState.RateItem) {
            titleTextView.text = item.code
            descriptionTextView.text = item.description
            iconImageView.setImageDrawable(item.icon)
            inputField.setText(item.amount.toString())
        }
    }
}