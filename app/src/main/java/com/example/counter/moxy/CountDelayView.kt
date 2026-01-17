package com.example.counter.moxy

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import moxy.viewstate.strategy.OneExecutionStateStrategy

interface CountDelayView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCount(value: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun saveCount(value: Int)
}
