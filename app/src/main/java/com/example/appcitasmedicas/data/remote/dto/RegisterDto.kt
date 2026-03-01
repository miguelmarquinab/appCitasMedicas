package com.example.appcitasmedicas.data.remote.dto

data class RegisterRequestDto(
    val idUsuario: Int? = 0,
    val idPersona: Int? = 0,
    val rolNombre: String? = "PACIENTE",
    val usuario: String,
    val clave: String,
    val language: String? = "es",
    val activo: Int? = 1,
    val eliminado: String? = "0"
)

data class RegisterResponseDto(
    val idUsuario: Int?,
    val usuario: String?,
    val rolNombre: String?
)