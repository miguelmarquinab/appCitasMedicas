package com.example.appcitasmedicas.data.remote

import retrofit2.Call;
import retrofit2.http.GET
import retrofit2.http.Query

data class LoginUserDto(
    val idUsuario: Int?,
    val idPersona: Int?,
    val rolNombre: String?,
    val usuario: String?
)

interface ApiService {

    @GET("User/Login")
    fun login(
        @Query("usuario") usuario: String,
        @Query("password") password: String
    ): Call<List<LoginUserDto>>
}