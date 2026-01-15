package com.example.counter.moxy

import kotlinx.coroutines.*
import moxy.MvpPresenter

class CountPresenter : MvpPresenter<CountDelayView>() {

    private val presenterScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

    private var count = 0

    fun getCount() = count

    fun buttonPlusOneClicked() {
        presenterScope.launch {
            delay(2000)
            viewState.showCount(++count)
        }
    }

    fun buttonMinusOneClicked() {
        presenterScope.launch {
            delay(2000)
            viewState.showCount(--count)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterScope.cancel()
    }
}
