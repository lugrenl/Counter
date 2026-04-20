package com.example.counter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.room.Room
import com.example.counter.databinding.CountRoomActivityBinding
import com.example.counter.room.Count
import com.example.counter.room.CountDatabase
import com.example.counter.utils.checkAndShowIntro
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

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
    private var disposables = CompositeDisposable()
    private val searchSubject = PublishSubject.create<String>()
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
        } else {
            // 2. ВТОРАЯ ОЧЕРЕДЬ (База данных Room с индексом)
            // Запускаем асинхронное получение данных с помощью RxJava.
            // Благодаря индексу в Entity, запрос к Room будет работать максимально быстро.
            disposables += countDao.getCounts(COUNT_KEY_NAME)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { list ->
                        val countEntity = list.firstOrNull()
                        if (countEntity != null) {
                            val dbValue = countEntity.value
                            if (count != dbValue) {
                                count = dbValue
                                binding.message.text = "$count"
                            }
                        }
                    },
                    onError = {
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    }
                )
        }

        binding.buttonMinus.setOnClickListener { updateCount(count - 1) }
        binding.buttonPlus.setOnClickListener { updateCount(count + 1) }

        setupSearchPipeline()

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            searchSubject.onNext(query)
        }

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            searchSubject.onNext(text?.toString() ?: "")
        }
    }

    private fun setupSearchPipeline() {
        disposables += searchSubject
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { query ->
                binding.searchResults.text = if (query.isBlank()) "" else "Searching..."
            }
            .debounce(300, TimeUnit.MILLISECONDS, Schedulers.computation())
            .distinctUntilChanged()
            .switchMapSingle { query ->
                if (query.isBlank()) {
                    Single.just(emptyList<Count>())
                } else {
                    countDao.searchByName(query)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturnItem(emptyList())
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { results ->
                    val currentQuery = binding.searchEditText.text.toString()
                    if (currentQuery.isBlank()) {
                        binding.searchResults.text = ""
                    } else {
                        binding.searchResults.text = if (results.isEmpty()) {
                            "No results"
                        } else {
                            results.joinToString("\n") { "${it.name}: ${it.value}" }
                        }
                    }
                },
                onError = {
                    Toast.makeText(this, "Search Error", Toast.LENGTH_LONG).show()
                }
            )
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun updateCount(newCount: Int) {
        count = newCount
        binding.message.text = "$count"
        saveCounterData(count)
    }

    private fun saveCounterData(value: Int) {
        // Благодаря индексу unique = true, OnConflictStrategy.REPLACE
        // будет работать как эффективное обновление кеш-записи
        val countEntity = Count(
            _id = 1, // Фиксированный ID для "кешированной" записи
            name = COUNT_KEY_NAME,
            value = value
        )
        disposables += countDao.setCount(countEntity)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, count)
    }
}