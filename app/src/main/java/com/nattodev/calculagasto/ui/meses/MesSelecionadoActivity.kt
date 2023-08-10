package com.nattodev.calculagasto.ui.meses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.databinding.ActivityMesSelecionadoBinding

class MesSelecionadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMesSelecionadoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMesSelecionadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}