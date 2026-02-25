package com.example.appcitasmedicas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.appcitasmedicas.databinding.ActivityBookingBinding
import com.example.appcitasmedicas.dto.DoctorDto
import com.example.appcitasmedicas.dto.ServicesDto
import com.example.appcitasmedicas.dto.SpecialtyDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding

    private lateinit var api: ApiService

    private var doctors = listOf<DoctorDto>()
    private var services = listOf<ServicesDto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRetrofit()
        setupSpinnersWithLoading()
        loadFormData()
    }


    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5213/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)
    }

    private fun setupSpinnersWithLoading() {

        binding.spinnerService.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Cargando servicios...")
        )

        binding.spinnerStage.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Cargando etapas...")
        )

    }


    private fun loadFormData() {
        loadServices()
        loadSpecialties()
    }

    private fun loadSpecialties() {
        lifecycleScope.launch {
            try {
                val especialidades = withContext(Dispatchers.IO) {
                    api.getSpecialties()
                }
                Log.d("API", "Documentos recibidos: ${especialidades.size}")
                updateSpinnerSpecialties(especialidades)
            } catch (e: Exception) {
                Log.e("API", "Error en especialidades: ${e.message}")
            }
        }
    }

    private fun updateSpinnerSpecialties(especialidades: List<SpecialtyDto>) {
        // Extraer solo los nombres para mostrar en el spinner
        val nombresEspecialidades = especialidades.map { it.nombre }

        // Crear adaptador con los nombres
        val adapter = ArrayAdapter(
            this,  // Contexto
            android.R.layout.simple_spinner_item,  // Layout para el item cerrado
            nombresEspecialidades  // Datos a mostrar
        )

        // Layout para cuando se despliega la lista
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar adaptador al spinner usando binding
        binding.spinnerStage.adapter = adapter
    }


    private fun loadServices() {
        lifecycleScope.launch {
            try {
                val servicios = withContext(Dispatchers.IO) {
                    api.getServices()
                }
                Log.d("API_OK", "Servicios recibidos: ${servicios.size}")
                updateSpinnerServices(servicios)

            } catch (e: Exception) {
                Log.e("API", "Error en Servicios: ${e.message}")
            }
        }
    }

    private fun updateSpinnerServices(servicios: List<ServicesDto>) {
        val nombresServicios = servicios.map { it.nombreServicio }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombresServicios
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerService.adapter = adapter
    }

}