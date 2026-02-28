package com.example.appcitasmedicas.dto

data class ReservarCitaRequest(
    val idPaciente: Int,
    val idHorario: Int,
    val observacion: String
)
