package com.dkonopelkin.revolutEntranceApp.core.domain

import io.reactivex.Observable

interface Storage<T> {
    fun observe(): Observable<T>
    fun update(data: T)
}