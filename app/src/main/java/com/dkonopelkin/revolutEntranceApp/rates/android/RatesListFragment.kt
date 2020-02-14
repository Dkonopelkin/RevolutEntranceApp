package com.dkonopelkin.revolutEntranceApp.rates.android

import android.app.AlertDialog
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rates.*


class RatesListFragment : Fragment() {

    private lateinit var viewModel: RatesViewModel
    private lateinit var ratesListAdapter: RatesListAdapter
    private val callback = object : RatesListAdapter.Callback {
        override fun onItemSelected(currencyCode: String, amount: String) {
            viewModel.onCurrencySelected(currencyCode, amount)
        }

        override fun onValueChanged(currencyCode: String, amount: String) {
            viewModel.onValueChanged(currencyCode, amount)
        }
    }

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

        ratesListAdapter = RatesListAdapter(arrayListOf(), requireContext(), callback)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ratesListAdapter
        }

        viewModel.stateLiveData.observe(
            this.viewLifecycleOwner,
            Observer { newState -> updateUIState(newState) })

        viewModel.errorLiveData.observe(
            this.viewLifecycleOwner,
            Observer { error ->
                showErrorInfo(error)
            })
    }

    private fun showErrorInfo(error: RatesViewModel.Error) {
        when (error) {
            is RatesViewModel.Error.NetworkError -> {
                showOfflineSnackbar()
            }
            is RatesViewModel.Error.ServerError -> {
                AlertDialog.Builder(requireActivity())
                    .setTitle(getString(R.string.error_dialog_title))
                    .setMessage(error.msg)
                    .setPositiveButton(getString(R.string.error_dialog_button_ok)) { view, _ -> view.dismiss() }
                    .show()
                showOfflineSnackbar()
            }
            is RatesViewModel.Error.NoError -> {
            }
        }
    }

    private fun showOfflineSnackbar() {
        Snackbar
            .make(
                coordinatorLayout,
                getString(R.string.snackbar_connection_lost),
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(getString(R.string.snackbar_retry_button)) {
                viewModel.onRetrySubscription()
            }.show()
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