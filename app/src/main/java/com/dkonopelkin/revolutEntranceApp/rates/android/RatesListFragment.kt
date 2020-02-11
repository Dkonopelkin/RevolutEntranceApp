package com.dkonopelkin.revolutEntranceApp.rates.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dkonopelkin.revolutEntranceApp.R
import com.dkonopelkin.revolutEntranceApp.rates.di.RatesViewModelFactory
import com.dkonopelkin.revolutEntranceApp.rates.presentation.RatesViewModel

class RatesListFragment : Fragment() {

    private lateinit var viewModel: RatesViewModel

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

    companion object {
        const val FRAGMENT_TAG = "RatesListFragment"

        fun newInstance(): RatesListFragment {
            return RatesListFragment()
        }
    }
}