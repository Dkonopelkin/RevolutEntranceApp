package com.dkonopelkin.revolutEntranceApp.rates.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dkonopelkin.revolutEntranceApp.R
import com.dkonopelkin.revolutEntranceApp.rates.di.RatesViewModelFactory
import com.dkonopelkin.revolutEntranceApp.rates.presentation.RatesViewModel
import kotlinx.android.synthetic.main.fragment_rates.*

class RatesListFragment : Fragment() {

    private lateinit var viewModel: RatesViewModel
    private lateinit var ratesListAdapter: RatesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, RatesViewModelFactory()).get(RatesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_rates, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : RatesListAdapter.Callback {
            override fun onItemSelected(currencyCode: String) {
                viewModel.onCurrencySelected(currencyCode)
            }

            override fun onValueChanged(currencyCode: String, newValue: String) {
                viewModel.onValueChanged(newValue)
            }
        }

        ratesListAdapter = RatesListAdapter(arrayListOf(), requireContext(), callback)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ratesListAdapter
        }

        viewModel.stateLiveData.observe(
            this.viewLifecycleOwner,
            Observer { newState -> updateUIState(newState) })
    }

    private fun updateUIState(state: RatesViewModel.UIState) {
        ratesListAdapter.updateItems(state.ratesList)
    }

    companion object {
        const val FRAGMENT_TAG = "RatesListFragment"

        fun newInstance(): RatesListFragment {
            return RatesListFragment()
        }
    }
}