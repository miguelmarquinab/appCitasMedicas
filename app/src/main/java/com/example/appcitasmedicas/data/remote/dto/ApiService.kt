package com.example.appcitasmedicas.data.remote.dto

import com.example.appcitasmedicas.data.remote.dto.ServicioDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("Servicios/listar")
    suspend fun listarServicios(): Response<List<ServicioDto>>

    @POST("User/register")
    fun register(@Body body: RegisterRequestDto): Call<RegisterResponseDto>
}