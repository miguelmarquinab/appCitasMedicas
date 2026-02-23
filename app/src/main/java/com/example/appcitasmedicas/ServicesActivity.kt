package com.example.appcitasmedicas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.appcitasmedicas.databinding.ActivityServicesBinding
class ServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardReservar.setOnClickListener {
            // luego: ir a reserva de cita
        }

        binding.cardMisCitas.setOnClickListener {
            // luego: ir a mis citas
        }


    }
}