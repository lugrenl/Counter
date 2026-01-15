package com.example.counter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class CountViewModel : ViewModel() {

    private val _count = MutableLiveData(0)

    val count: LiveData<Int> = _count

    private var countValue: Int
        get() = _count.value ?: 0
        set(value) = _count.postValue(value)

    fun countMinusOne() {
        viewModelScope.launch {
            delay(2000)
            countValue--
        }
    }

    fun countPlusOne() {
        viewModelScope.launch {
            delay(2000)
            countValue++
        }
    }
}
