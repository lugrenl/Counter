package com.example.counter

import android.os.Bundle
import com.example.counter.databinding.CountActivityBinding
import com.example.counter.moxy.CountDelayView
import com.example.counter.moxy.CountPresenter
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class CountDelayPrActivity : MvpAppCompatActivity(), CountDelayView {

    private val presenter by moxyPresenter { CountPresenter() }
    private lateinit var binding: CountActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showCount(presenter.getCount())

        binding.buttonMinus.setOnClickListener { presenter.buttonMinusOneClicked() }
        binding.buttonPlus.setOnClickListener { presenter.buttonPlusOneClicked() }
    }

    override fun showCount(value: Int) {
        binding.message.text = "$value"
    }
}