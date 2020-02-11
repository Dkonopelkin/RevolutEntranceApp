package com.dkonopelkin.revolutEntranceApp.rates.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dkonopelkin.revolutEntranceApp.rates.data.RatesApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RatesViewModel(
    ratesApiInterface: RatesApiInterface
) : ViewModel() {
    private val disposables = CompositeDisposable()

    init {
        disposables.add(ratesApiInterface.getRates("EUR")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, error ->
                Log.d("subscription:", result.toString())
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}