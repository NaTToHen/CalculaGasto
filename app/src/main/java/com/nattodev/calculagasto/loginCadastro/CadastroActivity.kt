package com.nattodev.calculagasto.loginCadastro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nattodev.calculagasto.R
import com.nattodev.calculagasto.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}