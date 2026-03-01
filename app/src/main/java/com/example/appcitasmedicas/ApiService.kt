package com.example.appcitasmedicas

import com.example.appcitasmedicas.dto.DoctorDto
import com.example.appcitasmedicas.dto.EspecialidadDto
import com.example.appcitasmedicas.dto.HorarioDto
import com.example.appcitasmedicas.dto.PacienteDto
import com.example.appcitasmedicas.dto.ReservarCitaRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("Especialidad/listar")
    suspend fun listarEspecialidades(): List<EspecialidadDto>

    @GET("User/paciente/{idUsuario}")
    suspend fun obtenerPaciente(@Path("idUsuario") idUsuario: Int): PacienteDto

    @GET("Medicos/especialidad/{idEspecialidad}")
    suspend fun obtenerMedicos(@Path("idEspecialidad") idEspecialidad: Int): List<DoctorDto>

    @GET("Medicos/horario")
    suspend fun obtenerHorarios(
        @Query("medico") idMedico: Int,
        @Query("fecha") fecha: String
    ): List<HorarioDto>

    @POST("ReservarCita/reservar")
    suspend fun reservarCita(@Body request: ReservarCitaRequest)
}