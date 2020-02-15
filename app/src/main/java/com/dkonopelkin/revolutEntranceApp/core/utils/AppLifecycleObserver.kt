package com.dkonopelkin.revolutEntranceApp.core.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class AppLifecycleObserver : LifecycleObserver {
    private val subject = BehaviorSubject.create<ApplicationState>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        subject.onNext(ApplicationState.STARTED)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        subject.onNext(ApplicationState.STOPPED)
    }

    fun observe(): Observable<ApplicationState> {
        return subject.toFlowable(BackpressureStrategy.LATEST).toObservable()
    }

    sealed class ApplicationState {
        object STARTED : ApplicationState()
        object STOPPED : ApplicationState()
    }
}