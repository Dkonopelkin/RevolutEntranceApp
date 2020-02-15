package com.dkonopelkin.revolutEntranceApp.rates.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dkonopelkin.revolutEntranceApp.core.utils.AppLifecycleObserver
import com.dkonopelkin.revolutEntranceApp.core.utils.parseBigDecimal
import com.dkonopelkin.revolutEntranceApp.core.utils.toFormattedString
import com.dkonopelkin.revolutEntranceApp.rates.domain.CurrencyStateStorage
import com.dkonopelkin.revolutEntranceApp.rates.domain.LoadRatesAndUpdateRepository
import com.dkonopelkin.revolutEntranceApp.rates.domain.UiStateObserver
import com.dkonopelkin.revolutEntranceApp.rates.domain.UpdateRatesIntervalObserver
import com.dkonopelkin.revolutEntranceApp.rates.types.CurrencyType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

class RatesViewModel(
    private val loadRatesAndUpdateRepository: LoadRatesAndUpdateRepository,
    private val currencyStateStorage: CurrencyStateStorage,
    private val uiStateObserver: UiStateObserver,
    private val updateRatesIntervalObserver: UpdateRatesIntervalObserver,
    appLifecycleObserver: AppLifecycleObserver
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private lateinit var ratesUpdateDisposable: Disposable

    val stateLiveData = MutableLiveData<UIState>()
    val errorLiveData = MutableLiveData<Error>()

    private lateinit var currencyState: CurrencyStateStorage.Currency

    init {
        observeCurrencyState()
        observeApplicationState(appLifecycleObserver)
        observeUiState()
    }

    private fun observeCurrencyState() {
        disposables.add(currencyStateStorage.observe().subscribe { baseCurrency ->
            currencyState = baseCurrency
        })
    }

    private fun observeApplicationState(appLifecycleObserver: AppLifecycleObserver) {
        disposables.add(appLifecycleObserver.observe().subscribe { appState ->
            if (appState == AppLifecycleObserver.ApplicationState.STOPPED) {
                disposeRatesSubscription()
            } else {
                updateRatesSubscription()
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        ratesUpdateDisposable.dispose()
    }

    fun onCurrencySelected(newCurrencyCode: String, amount: String) {
        if (newCurrencyCode != currencyState.code) {
            currencyStateStorage.update(
                CurrencyStateStorage.Currency(newCurrencyCode, amount.parseBigDecimal())
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

    fun updateRatesSubscription() {
        errorLiveData.value = Error.NoError

        ratesUpdateDisposable = updateRatesIntervalObserver.getObservable()
            .switchMapCompletable { baseCode -> loadRatesAndUpdateRepository.invoke(baseCode) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { throwable -> errorLiveData.value = mapErrors(throwable) })
    }

    private fun disposeRatesSubscription() {
        ratesUpdateDisposable.dispose()
    }

    private fun mapErrors(throwable: Throwable): Error {
        return when (throwable) {
            is UnknownHostException -> Error.NetworkError
            else -> Error.ServerError(throwable.toString())
        }
    }

    private fun mapUiState(sourceList: List<UiStateObserver.Currency>): UIState {
        val list = sourceList.map { currency ->
            val currencyInfo = CurrencyType.getByCurrencyCode(currency.code)
            UIState.RateItem(
                code = currency.code,
                description = currencyInfo.currencyDescription,
                amount = currency.amount.toFormattedString(),
                icon = currencyInfo.iconResId,
                isBaseCurrency = currency.isCurrent
            )
        }
        return UIState(list)
    }

    private fun observeUiState() {
        disposables.add(uiStateObserver.getObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> stateLiveData.value = mapUiState(result) },
                { throwable -> errorLiveData.value = mapErrors(throwable) }, {}
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