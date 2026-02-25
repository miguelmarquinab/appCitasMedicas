package com.example.appcitasmedicas.dto

data class DoctorDto(
    val idMedico: Long,
    val idEspecialidad: Long,
    val nombres: String,
    val apellidos: String,
    val cmp: String,
    val email: String
)
