package com.example.counter.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.counter.utils.KEY_COUNT
import com.example.counter.utils.PREFS_SAVE_NAME
import kotlinx.coroutines.*

class CountViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences(PREFS_SAVE_NAME, Context.MODE_PRIVATE)
    private val _count = MutableLiveData(prefs.getInt(KEY_COUNT, 0))
    val count: LiveData<Int> = _count

    fun countMinusOne() {
        updateCount(-1)
    }

    fun countPlusOne() {
        updateCount(1)
    }

    private fun updateCount(delta: Int) {
        viewModelScope.launch {
            delay(2000)

            val current = _count.value ?: 0
            val nextValue = current + delta

            // Используем .value вместо .postValue для мгновенного обновления в Main потоке.
            // Это гарантирует, что следующий вызов увидит уже обновленное значение.
            _count.value = nextValue

            prefs.edit().putInt(KEY_COUNT, nextValue).apply()
        }
    }
}
