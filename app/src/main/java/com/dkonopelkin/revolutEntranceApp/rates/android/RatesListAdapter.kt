package com.dkonopelkin.revolutEntranceApp.rates.android

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dkonopelkin.revolutEntranceApp.R
import com.dkonopelkin.revolutEntranceApp.rates.viewmodel.RatesViewModel


class RatesListAdapter(
    initialItems: List<RatesViewModel.UIState.RateItem>,
    private val context: Context,
    private val callback: Callback
) : RecyclerView.Adapter<RatesListViewHolder>() {

    private var items = initialItems

    fun updateItems(newItems: List<RatesViewModel.UIState.RateItem>) {
        val diffCallback = RatesDiffUtilCallback(items, newItems)
        items = newItems
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rates, parent, false)
        return RatesListViewHolder(containerView = view, callback = callback)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RatesListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(holder: RatesListViewHolder, position: Int, payloads: MutableList<Any>) {
        holder.bind(items[position])
    }

    interface Callback {
        fun onItemSelected(currencyCode: String, amount: String)
        fun onValueChanged(currencyCode: String, amount: String)
    }

    class RatesDiffUtilCallback(
        private val oldList: List<RatesViewModel.UIState.RateItem>,
        private val newList: List<RatesViewModel.UIState.RateItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].code == newList[newItemPosition].code

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? = Unit
    }

}