package com.example.counter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.counter.databinding.CountActivityBinding
import com.example.counter.utils.KEY_COUNT
import com.example.counter.utils.PREFS_SAVE_NAME
import com.example.counter.utils.checkAndShowIntro

class CountDelayActivity : AppCompatActivity() {

    private var count = 0
    private lateinit var binding: CountActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndShowIntro()

        binding = CountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt(KEY_COUNT)
        } else {
            count = getCounterData(this)
        }

        binding.message.text = "$count"

        binding.buttonMinus.setOnClickListener { updateCount(count - 1) }
        binding.buttonPlus.setOnClickListener { updateCount(count + 1) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, count)
        saveCounterData(this, count)
    }

    private fun updateCount(newCount: Int) {
        Handler(mainLooper).postDelayed({
            count = newCount
            binding.message.text = "$count"
            println(binding.message)
            saveCounterData(this, count)
        }, 2000)
    }

    private fun getCounterData(context: Context): Int {
        val prefs = getSharedPreferences(PREFS_SAVE_NAME, Context.MODE_PRIVATE)

        return prefs.getInt(KEY_COUNT, 0)
    }

    private fun saveCounterData(context: Context,value: Int) {
        val prefs = getSharedPreferences(PREFS_SAVE_NAME, Context.MODE_PRIVATE)

        prefs.edit().putInt(KEY_COUNT, value).apply()
    }
}