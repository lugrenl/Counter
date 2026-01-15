package com.example.counter

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.counter.databinding.CountActivityBinding

class CountDelayActivity : AppCompatActivity() {

    companion object {
        private const val KEY_COUNT = "KEY_COUNT"
    }

    private var count = 0
    private lateinit var binding: CountActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt(KEY_COUNT)
        }

        binding.message.text = "$count"

        binding.buttonMinus.setOnClickListener { updateCount(count - 1) }
        binding.buttonPlus.setOnClickListener { updateCount(count + 1) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, count)
    }

    private fun updateCount(newCount: Int) {
        Handler(mainLooper).postDelayed({
            count = newCount
            binding.message.text = "$count"
            println(binding.message)
        }, 2000)
    }
}