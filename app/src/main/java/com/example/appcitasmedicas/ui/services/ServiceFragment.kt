package com.example.appcitasmedicas.ui.services

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcitasmedicas.BookingActivity
import com.example.appcitasmedicas.LoginActivity
import com.example.appcitasmedicas.R
import com.example.appcitasmedicas.databinding.FragmentServiceBinding
import com.example.appcitasmedicas.ui.services.adapter.ServiceAdapter
import com.example.appcitasmedicas.ui.services.model.ServiceItem

class ServiceFragment :  Fragment(R.layout.fragment_service) {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentServiceBinding.bind(view)

        // Nombre (puedes traerlo del login luego)
        binding.tvHola.text = "Hola Anthony"

        val items = listOf(
            ServiceItem("RESERVAR", "Reservar cita", "Selecciona especialidad, fecha y horario"),
            ServiceItem("MIS_CITAS", "Mis citas", "Revisa tus citas programadas"),
            ServiceItem("HISTORIAL", "Historial", "Consulta atenciones anteriores")
        )

        binding.rvServices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvServices.adapter = ServiceAdapter(items) { item ->
            when (item.id) {
                "RESERVAR" -> {
                    // TODO: navegar a pantalla reservar
                    val intent = Intent(requireContext(), BookingActivity::class.java)
                    startActivity(intent)
                }
                "MIS_CITAS" -> {
                    // TODO
                }
            }
        }

        // Logout (por ahora vuelve a Login limpiando stack)
        binding.btnLogout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}