package com.dkonopelkin.revolutEntranceApp.rates.presentation

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dkonopelkin.revolutEntranceApp.core.di.AppInjector.appDependencies
import com.dkonopelkin.revolutEntranceApp.rates.domain.RatesRepository
import com.dkonopelkin.revolutEntranceApp.rates.interactors.LoadRatesAndSave
import com.dkonopelkin.revolutEntranceApp.rates.types.CurrencyType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

class RatesViewModel(
    private val loadRatesAndSave: LoadRatesAndSave,
    private val ratesRepository: RatesRepository
) : ViewModel() {

    val stateLiveData = MutableLiveData<UIState>()
    private val disposables = CompositeDisposable()

    private var currentBase: String = "EUR"
    private var currentCount: BigDecimal = BigDecimal(100)
    private val baseCurrencyStateSubject =
        BehaviorSubject.createDefault(Currency(currentBase, currentCount))

    data class Currency(val code: String, val amount: BigDecimal)

    init {
        updateRatesSubscription()
        updateUiSubscription()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun onCurrencySelected(currencyCode: String) {
        currentBase = currencyCode
        baseCurrencyStateSubject.onNext(Currency(currentBase, currentCount))
    }

    fun onValueChanged(newValue: String) {
        currentCount = newValue.toBigDecimal()
        baseCurrencyStateSubject.onNext(Currency(currentBase, currentCount))
    }

    private fun updateRatesSubscription() {

        val observableUpdateSignal: Observable<Long> =
            Observable.interval(5, TimeUnit.SECONDS).startWith(0)

        val subscription = Observable
            .combineLatest<Long, Currency, String>(
                observableUpdateSignal,
                baseCurrencyStateSubject,
                BiFunction { _, currency -> currency.code }
            )
            .switchMapCompletable { baseCode ->
                loadRatesAndSave.invoke(baseCode)
            }


        disposables.add(subscription
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.d("onComplete", "done") },
                { throwable -> Log.d("onError", throwable.toString()) }
            )
        )

    }

    private fun updateUiSubscription() {

        fun mapUiState(sourceList: List<Currency>): UIState {
            val list = sourceList.map { currency ->
                val currencyInfo = CurrencyType.getByCurrencyCode(currency.code)
                UIState.RateItem(
                    code = currency.code,
                    description = currencyInfo.currencyDescription,
                    amount = currency.amount,
                    icon = ContextCompat.getDrawable(
                        appDependencies.context,
                        currencyInfo.iconResId
                    )!!,
                    isBaseCurrency = false
                )
            }
            return UIState(list)
        }

        val subscription = baseCurrencyStateSubject.switchMap { baseCurrency ->
            Observable.combineLatest(
                Observable.just(baseCurrency),
                ratesRepository.observeRatesByCode(baseCurrency.code),
                BiFunction<Currency, Map<String, BigDecimal>, List<Currency>> { currency, rates ->
                    // TODO .scan or .collect
                    val mutableList = mutableListOf<Currency>()
                    rates.keys.forEach { code ->
                        val amountRatio = rates[code]!! * baseCurrency.amount
                        mutableList.add(Currency(code, amountRatio))
                    }
                    mutableList.add(0, currency)
                    mutableList
                })
        }

        disposables.add(subscription
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d("onNext", result.toString())
                    stateLiveData.value = mapUiState(result)
                },
                { throwable -> Log.d("onError", throwable.toString()) },
                { Log.d("onComplete", "done") }
            )
        )
    }

    private fun recalculateValue() {

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