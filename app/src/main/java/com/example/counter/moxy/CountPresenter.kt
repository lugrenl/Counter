package com.example.counter.moxy

import kotlinx.coroutines.*
import moxy.MvpPresenter

class CountPresenter : MvpPresenter<CountDelayView>() {

    private val presenterScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }
    private var count = 0

    fun initCount(value: Int) {
        count = value
        viewState.showCount(count)
    }

    fun buttonPlusOneClicked() {
        presenterScope.launch {
            delay(2000)
            count++
            viewState.showCount(count)
            viewState.saveCount(count)
        }
    }

    fun buttonMinusOneClicked() {
        presenterScope.launch {
            delay(2000)
            count--
            viewState.showCount(count)
            viewState.saveCount(count)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterScope.cancel()
    }
}
