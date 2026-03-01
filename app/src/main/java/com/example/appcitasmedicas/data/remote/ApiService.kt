package com.example.appcitasmedicas.data.remote

import retrofit2.Call;
import retrofit2.http.GET
import retrofit2.http.Query

data class LoginResponse(
    val mensaje: String,
    val usuario: UsuarioDto
)

data class UsuarioDto(
    val idUsuario: Int,
    val username: String,
    val idRol: Int,
    val rolNombre: String,
    val activo: Boolean
)

interface ApiService {

    @GET("User/Login")
    fun login(
        @Query("usuario") usuario: String,
        @Query("password") password: String
    ): Call<LoginResponse>
}