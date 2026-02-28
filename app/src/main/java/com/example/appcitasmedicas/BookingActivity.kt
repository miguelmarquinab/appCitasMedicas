package com.example.appcitasmedicas

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appcitasmedicas.databinding.ActivityBookingBinding
import com.example.appcitasmedicas.dto.DoctorDto
import com.example.appcitasmedicas.dto.ServicesDto
import com.example.appcitasmedicas.dto.EspecialidadDto
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import com.example.appcitasmedicas.dto.ReservarCitaRequest
import java.util.Calendar


class BookingActivity : AppCompatActivity() {

    private lateinit var editNames: EditText
    private lateinit var editLastName: EditText
    private lateinit var editDocument: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var editDate: EditText
    private lateinit var editObservation: EditText
    private lateinit var spinnerSpecialty: Spinner
    private lateinit var spinnerDoctor: Spinner
    private lateinit var btnHora9: Button
    private lateinit var btnHora10: Button
    private lateinit var btnHora11: Button
    private lateinit var btnReservar: Button

    private var mapMedicos: Map<String, Int> = emptyMap()

    private lateinit var api: ApiService
    private var idPaciente: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        editNames = findViewById(R.id.editNames)
        editLastName = findViewById(R.id.editLastName)
        editDocument = findViewById(R.id.editDocument)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        editDate = findViewById(R.id.editDate)
        editObservation = findViewById(R.id.editObservation)
        spinnerSpecialty = findViewById(R.id.spinnerSpecialty)
        spinnerDoctor = findViewById(R.id.spinnerDoctor)
        btnHora9 = findViewById(R.id.btnHora9)
        btnHora10 = findViewById(R.id.btnHora10)
        btnHora11 = findViewById(R.id.btnHora11)
        btnReservar = findViewById(R.id.btnReservar)


        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5213/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        api = retrofit.create(ApiService::class.java)



        val prefs = getSharedPreferences("personal_data", Context.MODE_PRIVATE)
        val idUsuario = prefs.getInt("ID_USUARIO", -1)

        if (idUsuario != -1) {
            lifecycleScope.launch {
                try {
                    val paciente = api.obtenerPaciente(idUsuario)
                    idPaciente = paciente.idPaciente
                    editNames.setText(paciente.nombres)
                    editLastName.setText(paciente.apellidos)
                    editDocument.setText(paciente.documento)
                    editPhone.setText(paciente.celular)
                    editEmail.setText(paciente.email)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@BookingActivity, "Error al cargar paciente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            cargarEspecialidades()
        }

        editDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val fechaSeleccionada = "%04d-%02d-%02d".format(year, month + 1, day)
                    Log.d("FECHA_SELECCIONADA", fechaSeleccionada)
                    editDate.setText(fechaSeleccionada)
                    val idMedico = getIdMedicoSeleccionado()
                    if (idMedico != null) {
                        lifecycleScope.launch { cargarHorarios(idMedico, fechaSeleccionada) }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        btnReservar.setOnClickListener {
            val idHorario = listOf(btnHora9, btnHora10, btnHora11).find { it.isSelected }?.tag as? Int
            val observacion = editObservation.text.toString()

            if (idHorario != null && idPaciente != -1) {
                lifecycleScope.launch {
                    try {
                        val request = ReservarCitaRequest(idPaciente, idHorario, observacion)
                        api.reservarCita(request)
                        Toast.makeText(this@BookingActivity, "Cita reservada!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@BookingActivity, ServicesActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@BookingActivity, "Error al reservar", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Seleccione una hora", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun cargarEspecialidades() {
        try {
            val especialidades = api.listarEspecialidades()
            val nombres = especialidades.map { it.nombre }
            val mapId = especialidades.associateBy({ it.nombre }, { it.idEspecialidad })

            val adapter = ArrayAdapter(this@BookingActivity, android.R.layout.simple_spinner_item, nombres)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSpecialty.adapter = adapter

            spinnerSpecialty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val idEspecialidad = mapId[nombres[position]]!!
                    lifecycleScope.launch { cargarMedicos(idEspecialidad) }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun cargarMedicos(idEspecialidad: Int) {
        try {
            val medicos = api.obtenerMedicos(idEspecialidad)
            val nombres = medicos.map { "${it.nombres} ${it.apellidos}" }
            val mapId = medicos.associateBy({ "${it.nombres} ${it.apellidos}" }, { it.idMedico })

            mapMedicos = medicos.associateBy(
                {"${it.nombres} ${it.apellidos}"},
                {it.idMedico}
            )

            val adapter = ArrayAdapter(this@BookingActivity, android.R.layout.simple_spinner_item, nombres)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDoctor.adapter = adapter

            spinnerDoctor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    limpiarHoras()
                    val fecha = editDate.text.toString()
                    val idMedico = getIdMedicoSeleccionado()
                    if(fecha.isNotEmpty() && idMedico != null){
                        lifecycleScope.launch { cargarHorarios(idMedico, fecha) }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun cargarHorarios(idMedico: Int, fecha: String) {
        try {
            val horarios = api.obtenerHorarios(idMedico, fecha)
            val botones = listOf(btnHora9, btnHora10, btnHora11)
            limpiarHoras()
            horarios.take(botones.size).forEachIndexed { index, h ->
                val boton = botones[index]
                boton.text = h.horaInicio.substring(0,5)
                boton.tag = h.idHorario
                boton.isEnabled = h.cuposDisponibles > 0
                boton.setBackgroundColor(if (h.cuposDisponibles > 0) Color.LTGRAY else Color.RED)
                boton.setOnClickListener {
                    botones.forEach { it.isSelected = false; it.setBackgroundColor(Color.LTGRAY) }
                    boton.isSelected = true
                    boton.setBackgroundColor(Color.GREEN)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun limpiarHoras() {
        val botones = listOf(btnHora9, btnHora10, btnHora11)
        botones.forEach {
            it.text = "--:--"
            it.tag = null
            it.isEnabled = false
            it.setBackgroundColor(Color.LTGRAY)
            it.isSelected = false
        }
    }

    private fun getIdMedicoSeleccionado(): Int? {
        val nombre = spinnerDoctor.selectedItem as? String
        return nombre?.let { mapMedicos[it] }
    }
}