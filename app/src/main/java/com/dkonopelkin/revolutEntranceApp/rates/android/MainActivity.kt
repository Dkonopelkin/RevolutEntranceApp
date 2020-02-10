package com.dkonopelkin.revolutEntranceApp.rates.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dkonopelkin.revolutEntranceApp.R
import com.dkonopelkin.revolutEntranceApp.core.di.appDependencies
import com.dkonopelkin.revolutEntranceApp.rates.data.RatesApiInterface
import com.dkonopelkin.revolutEntranceApp.rates.di.DaggerRatesComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var ratesApiInterface: RatesApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val component = DaggerRatesComponent.factory().create(appDependencies = appDependencies)
        component.inject(this)

        val singleResponce = ratesApiInterface.getRates("EUR")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, error ->
                Log.d("subscription:", result.toString())
            }
    }
}
