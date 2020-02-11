package com.dkonopelkin.revolutEntranceApp.rates.presentation

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dkonopelkin.revolutEntranceApp.core.di.AppInjector
import com.dkonopelkin.revolutEntranceApp.rates.domain.LoadRatesGateway
import com.dkonopelkin.revolutEntranceApp.rates.types.CurrencyType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

class RatesViewModel(
    loadRatesGateway: LoadRatesGateway
) : ViewModel() {

    val stateLiveData = MutableLiveData<UIState>()
    private val disposables = CompositeDisposable()

    init {
        disposables.add(
            loadRatesGateway.loadRates("EUR")
                .toFlowable()
                .flatMapIterable { data -> data.rates.toList() }
                .map { pair -> CurrencyType.getByCurrencyCode(pair.first) }
                .map { currency ->
                    Log.d("map", currency.toString())
                    UIState.RateItem(
                        code = currency.currencyCode,
                        description = currency.currencyDescription,
                        amount = BigDecimal.ZERO,
                        icon = ContextCompat.getDrawable(
                            AppInjector.appDependencies.context,
                            currency.iconResId
                        )
                            ?: throw IllegalArgumentException("Resource for ${currency.currencyCode} not found"),
                        isBaseCurrency = false
                    )
                }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data: List<UIState.RateItem>?, error: Throwable? ->
                    if (data != null) {
                        stateLiveData.value = UIState(data)
                    }
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }


    data class UIState(
        val ratesList: List<RateItem>
    ) {
        data class RateItem(
            val code: String,
            val description: String,
            val amount: BigDecimal,
            val icon: Drawable,
            val isBaseCurrency: Boolean
        )
    }
}