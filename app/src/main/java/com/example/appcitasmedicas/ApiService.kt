package com.example.appcitasmedicas

import com.example.appcitasmedicas.dto.ServicesDto
import com.example.appcitasmedicas.dto.SpecialtyDto
import retrofit2.http.GET
interface ApiService {
    @GET("Servicios/listar")
    suspend fun getServices(): List<ServicesDto>

    @GET("Especialidad/listar")
    suspend fun getSpecialties(): List<SpecialtyDto>
}