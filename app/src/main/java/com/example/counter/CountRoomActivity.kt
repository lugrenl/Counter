package com.example.counter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.counter.databinding.CountRoomActivityBinding
import com.example.counter.room.Count
import com.example.counter.room.CountDatabase
import com.example.counter.utils.checkAndShowIntro
import kotlinx.coroutines.launch

class CountRoomActivity : AppCompatActivity() {

    companion object {
        private const val DB_NAME = "my_db_name"
        private const val KEY_COUNT = "KEY_COUNT"
        private const val COUNT_KEY_NAME = "count_x"
    }

    private val countDatabase by lazy {
        Room.databaseBuilder(this, CountDatabase::class.java, DB_NAME).build()
    }

    private val countDao by lazy { countDatabase.countDao() }

    private var count = 0
    private lateinit var binding: CountRoomActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndShowIntro()

        binding = CountRoomActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. ПЕРВАЯ ОЧЕРЕДЬ (Кеш приложения/системы)
        // Сначала берем значение из savedInstanceState, чтобы пользователь увидел его мгновенно
        if (savedInstanceState != null) {
            count = savedInstanceState.getInt(KEY_COUNT)
            binding.message.text = "$count"
        }

        // 2. ВТОРАЯ ОЧЕРЕДЬ (База данных Room с индексом)
        // Запускаем асинхронное получение данных. Благодаря индексу в Entity,
        // запрос к Room будет работать максимально быстро.
        lifecycleScope.launch {
            val counts = countDao.getCounts(COUNT_KEY_NAME)
            if (counts.isNotEmpty()) {
                val dbValue = counts.first().value

                // Если данные в БД отличаются от того, что мы показали из "кеша", обновляем UI
                if (count != dbValue) {
                    count = dbValue
                    binding.message.text = "$count"
                }
            }
        }

        binding.buttonMinus.setOnClickListener { updateCount(count - 1) }
        binding.buttonPlus.setOnClickListener { updateCount(count + 1) }

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            performSearch(query)
        }
    }

    private fun updateCount(newCount: Int) {
        count = newCount
        binding.message.text = "$count"
        saveCounterData(count)
    }

    private fun saveCounterData(value: Int) {
        lifecycleScope.launch {
            // Благодаря индексу unique = true, OnConflictStrategy.REPLACE
            // будет работать как эффективное обновление кеш-записи
            val countEntity = Count(
                _id = 1, // Фиксированный ID для "кешированной" записи
                name = COUNT_KEY_NAME,
                value = value
            )
            countDao.setCount(countEntity)
        }
    }

    private fun performSearch(query: String) {
        lifecycleScope.launch {
            val results = countDao.searchByName(query)
            binding.searchResults.text = if (results.isEmpty()) "No results" else
                results.joinToString("\n") { "${it.name}: ${it.value}" }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, count)
    }
}