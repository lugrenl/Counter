package com.example.counter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.counter.databinding.IntroActivityBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: IntroActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IntroActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonContinue.setOnClickListener {
            finish()
        }
    }
}
