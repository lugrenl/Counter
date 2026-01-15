package com.example.counter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.counter.databinding.CountActivityBinding

import com.example.counter.viewmodel.CountViewModel


class CountDelayVmActivity : AppCompatActivity() {

    private val viewModel: CountViewModel by viewModels()
    private lateinit var binding: CountActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CountActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.count.observe(this) { binding.message.text = "$it" }

        binding.buttonMinus.setOnClickListener { viewModel.countMinusOne() }
        binding.buttonPlus.setOnClickListener { viewModel.countPlusOne() }
    }
}