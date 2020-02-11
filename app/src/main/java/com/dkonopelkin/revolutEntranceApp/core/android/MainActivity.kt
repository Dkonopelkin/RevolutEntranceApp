package com.dkonopelkin.revolutEntranceApp.core.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dkonopelkin.revolutEntranceApp.R
import com.dkonopelkin.revolutEntranceApp.rates.android.RatesListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = RatesListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragment, RatesListFragment.FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }
}
