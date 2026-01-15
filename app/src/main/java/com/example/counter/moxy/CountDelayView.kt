package com.example.counter.moxy

import moxy.MvpView
import moxy.viewstate.strategy.StateStrategyType
import moxy.viewstate.strategy.OneExecutionStateStrategy

interface CountDelayView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCount(value: Int)
}
