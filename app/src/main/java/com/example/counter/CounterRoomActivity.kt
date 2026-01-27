package com.example.counter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.counter.databinding.CountActivityBinding
import com.example.counter.room.Count
import com.example.counter.room.CountDatabase
import com.example.counter.utils.checkAndShowIntro
import kotlinx.coroutines.launch


class CountRoomActivity : AppCompatActivity() {

    companion object {
        private const val DB_NAME = "my_db_name"
        private const val KEY_COUNT = "KEY_COUNT"
    }

    private val countDatabase by lazy {
        Room.databaseBuilder(this, CountDatabase::class.java, DB_NAME).build()
    }

    private val countDao by lazy { countDatabase.countDao() }

    private var count = 0
    private lateinit var binding: CountActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndShowIntro()

        binding = CountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            if (savedInstanceState != null) {
                count = savedInstanceState.getInt(KEY_COUNT)
            } else {
                count = countDao.getCounts("count_x").first().value
            }

            binding.message.text = "$count"
        }

        binding.buttonMinus.setOnClickListener { updateCount(count - 1) }
        binding.buttonPlus.setOnClickListener { updateCount(count + 1) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, count)
    }

    private fun updateCount(newCount: Int) {
        count = newCount
        binding.message.text = "$count"
        saveCounterData(count)
    }

    private fun saveCounterData(value: Int) {
        lifecycleScope.launch {
            val countEntity = Count(
                _id = 1,
                name = "count_x",
                value = count
            )

            countDao.setCount(countEntity)
        }
    }
}
