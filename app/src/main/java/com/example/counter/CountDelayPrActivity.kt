package com.example.counter

import android.content.Context
import android.os.Bundle
import com.example.counter.databinding.CountActivityBinding
import com.example.counter.moxy.CountDelayView
import com.example.counter.moxy.CountPresenter
import com.example.counter.utils.KEY_COUNT
import com.example.counter.utils.PREFS_SAVE_NAME
import com.example.counter.utils.checkAndShowIntro
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class CountDelayPrActivity : MvpAppCompatActivity(), CountDelayView {

    private val presenter by moxyPresenter { CountPresenter() }
    private lateinit var binding: CountActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndShowIntro()

        binding = CountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedCount = getSharedPreferences(PREFS_SAVE_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_COUNT, 0)

        presenter.initCount(savedCount)

        binding.buttonMinus.setOnClickListener { presenter.buttonMinusOneClicked() }
        binding.buttonPlus.setOnClickListener { presenter.buttonPlusOneClicked() }
    }

    override fun showCount(value: Int) {
        binding.message.text = "$value"
    }

    override fun saveCount(value: Int) {
        getSharedPreferences(PREFS_SAVE_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_COUNT, value)
            .apply()
    }
}