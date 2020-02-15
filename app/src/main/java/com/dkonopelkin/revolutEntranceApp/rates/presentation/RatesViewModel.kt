package com.dkonopelkin.revolutEntranceApp.rates.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dkonopelkin.revolutEntranceApp.core.utils.AppLifecycleObserver
import com.dkonopelkin.revolutEntranceApp.core.utils.parseBigDecimal
import com.dkonopelkin.revolutEntranceApp.core.utils.toFormattedString
import com.dkonopelkin.revolutEntranceApp.rates.domain.CurrencyStateStorage
import com.dkonopelkin.revolutEntranceApp.rates.domain.RatesRepository
import com.dkonopelkin.revolutEntranceApp.rates.interactors.LoadRatesAndSave
import com.dkonopelkin.revolutEntranceApp.rates.types.CurrencyType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class RatesViewModel(
    private val loadRatesAndSave: LoadRatesAndSave,
    private val ratesRepository: RatesRepository,
    private val currencyStateStorage: CurrencyStateStorage,
    appLifecycleObserver: AppLifecycleObserver
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private lateinit var ratesUpdateDisposable: Disposable

    val stateLiveData = MutableLiveData<UIState>()
    val errorLiveData = MutableLiveData<Error>()

    private lateinit var currencyState: CurrencyStateStorage.Currency
    private val baseCurrencyObserver = currencyStateStorage.observe()

    init {
        disposables.add(baseCurrencyObserver.subscribe { baseCurrency ->
            currencyState = baseCurrency
        })

        disposables.add(appLifecycleObserver.observe().subscribe { appState ->
            if (appState == AppLifecycleObserver.ApplicationState.STOPPED) {
                disposeRatesSubscription()
            } else {
                updateRatesSubscription()
            }
        })

        updateUiSubscription()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        ratesUpdateDisposable.dispose()
    }

    fun onCurrencySelected(currencyCode: String, amount: String) {
        if (currencyCode != currencyState.code) {
            currencyStateStorage.update(
                CurrencyStateStorage.Currency(currencyCode, amount.parseBigDecimal())
            )
        }
    }

    fun onValueChanged(currencyCode: String, amount: String) {
        if (amount.parseBigDecimal() != currencyState.amount) {
            currencyStateStorage.update(
                CurrencyStateStorage.Currency(currencyCode, amount.parseBigDecimal())
            )
        }
    }

    fun onRetrySubscription() {
        errorLiveData.value = Error.NoError
        updateRatesSubscription()
    }

    private fun updateRatesSubscription() {
        val intervalUpdateSignal = Observable.interval(5, TimeUnit.SECONDS).startWith(0)

        ratesUpdateDisposable = Observable
            .combineLatest<Long, CurrencyStateStorage.Currency, String>(
                intervalUpdateSignal,
                baseCurrencyObserver.observeOn(Schedulers.io()),
                BiFunction { _, currency -> currency.code }
            )
            .switchMapCompletable { baseCode ->
                loadRatesAndSave.invoke(baseCode)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { throwable ->
                Log.d("onError", throwable.toString())
                errorLiveData.value = mapErrors(throwable)
            })
    }

    private fun disposeRatesSubscription() {
        ratesUpdateDisposable.dispose()
    }

    private fun mapErrors(throwable: Throwable): Error {
        return when (throwable) {
            is UnknownHostException -> {
                Error.NetworkError
            }
            else -> {
                Error.ServerError(throwable.toString())
            }
        }
    }

    private fun mapUiState(sourceList: List<CurrencyStateStorage.Currency>): UIState {
        val list = sourceList.mapIndexed { index, currency ->
            val currencyInfo = CurrencyType.getByCurrencyCode(currency.code)
            UIState.RateItem(
                code = currency.code,
                description = currencyInfo.currencyDescription,
                amount = currency.amount.toFormattedString(),
                icon = currencyInfo.iconResId,
                isBaseCurrency = index == 0
            )
        }
        return UIState(list)
    }

    private fun updateUiSubscription() {

        val subscription = baseCurrencyObserver.switchMap { baseCurrency ->
            Observable.combineLatest(
                Observable.just(baseCurrency),
                ratesRepository.observeRatesByCode(baseCurrency.code),
                BiFunction<CurrencyStateStorage.Currency, Map<String, BigDecimal>, List<CurrencyStateStorage.Currency>> { currency, rates ->
                    // TODO .scan or .collect
                    val mutableList = mutableListOf<CurrencyStateStorage.Currency>()
                    rates.keys.forEach { code ->
                        val amountRatio = rates[code]!! * baseCurrency.amount
                        mutableList.add(CurrencyStateStorage.Currency(code, amountRatio))
                    }
                    mutableList.add(0, currency)
                    mutableList
                })
        }

/*
        val subscription = baseCurrencyObserver
            .switchMap { baseCurrency ->
                ratesRepository.observeRatesByCode(baseCurrency.code)
                    .flatMap { ratesMap: Map<String, BigDecimal> -> Observable.fromIterable(ratesMap.toList()) }
                    .map { (code, ratio) ->
                        CurrencyStateStorage.Currency(code, ratio * baseCurrency.amount)
                    }
                    .toList()
                    .map { currencyList -> currencyList.add(0, baseCurrency) }
                    .toObservable()
            }
*/

        disposables.add(subscription
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d("onNext", result.toString())
                    stateLiveData.value = mapUiState(result)
                },
                { throwable ->
                    Log.d("onError", throwable.toString())
                    //throw(throwable)
                },
                { Log.d("onComplete", "done") }
            )
        )
    }

    data class UIState(
        val ratesList: List<RateItem>
    ) {
        data class RateItem(
            val code: String,
            val description: String,
            val amount: String,
            val icon: Int,
            val isBaseCurrency: Boolean
        )
    }

    sealed class Error {
        object NoError : Error()
        object NetworkError : Error()
        data class ServerError(val msg: String) : Error()
    }
}