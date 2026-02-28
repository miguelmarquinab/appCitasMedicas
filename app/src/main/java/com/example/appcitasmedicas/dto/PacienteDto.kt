package com.example.appcitasmedicas.dto

data class PacienteDto(
    val idPaciente: Int,
    val idUsuario: Int,
    val nombres: String,
    val apellidos: String,
    val documento: String,
    val fechaNacimiento: String,
    val sexo: String,
    val celular: String,
    val email: String
)
