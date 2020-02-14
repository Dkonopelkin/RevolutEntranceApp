package com.dkonopelkin.revolutEntranceApp.core.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dkonopelkin.revolutEntranceApp.R
import com.dkonopelkin.revolutEntranceApp.rates.android.RatesListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFragmentExists =
            supportFragmentManager.findFragmentByTag(RatesListFragment.FRAGMENT_TAG) != null
        if (!isFragmentExists) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    RatesListFragment.newInstance(),
                    RatesListFragment.FRAGMENT_TAG
                )
                .commitAllowingStateLoss()
        }
    }
}
