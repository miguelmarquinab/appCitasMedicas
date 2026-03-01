package com.example.appcitasmedicas.dto

data class HorarioDto(
    val idHorario: Int,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val cuposDisponibles: Int,
    val servicio: String
)
