package com.example.appcitasmedicas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.appcitasmedicas.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1) "Inflamos" el XML a una clase Binding generada automáticamente
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // 2) Mostramos el layout correcto (NO uses setContentView(R.layout...) aquí)
        setContentView(binding.root)

        // 3) Estado inicial del botón
        updateLoginButtonState()

        // 4) Cada vez que el usuario escriba, re-validamos para UX (botón se habilita solo si es válido)
        binding.etEmail.addTextChangedListener { updateLoginButtonState() }
        binding.etPassword.addTextChangedListener { updateLoginButtonState() }

        // 5) Click en "Ingresar"
        binding.btnLogin.setOnClickListener {
            val usuario = binding.etEmail.text?.toString()?.trim().orEmpty()
            var clave = binding.etPassword.text?.toString().orEmpty()

            if (usuario.isBlank()) {
                binding.tilEmail.error = "Ingrese usuario"
                return@setOnClickListener
            }

            if (clave.length < 4) {
                binding.tilPassword.error = "Clave invalida"
                return@setOnClickListener
            }
            doLogin(usuario, clave)
        }

        // 6) Click en "Registrarme" (por ahora solo mensaje)
        binding.btnGoRegister.setOnClickListener {
            binding.tvError.text = "Registro aún no implementado (siguiente paso)."
            binding.tvError.visibility = View.VISIBLE
        }
    }


    private fun doLogin(usuario: String, clave: String) {
        setLoading(true)
        binding.tvError.visibility = View.GONE
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        com.example.appcitasmedicas.data.remote.ApiClient.api
            .login(usuario, clave)
            .enqueue(object : retrofit2.Callback<List<com.example.appcitasmedicas.data.remote.LoginUserDto>> {

                override fun onResponse(
                    call: retrofit2.Call<List<com.example.appcitasmedicas.data.remote.LoginUserDto>>,
                    response: retrofit2.Response<List<com.example.appcitasmedicas.data.remote.LoginUserDto>>
                ) {
                    setLoading(false)

                    if (!response.isSuccessful) {
                        showError("Error HTTP: ${response.code()}")
                        return
                    }

                    val body = response.body().orEmpty()
                    if (body.isEmpty()) {
                        showError("Usuario o clave incorrectos")
                        return
                    }

                    // ✅ Login OK
                    goToServices()
                }

                override fun onFailure(
                    call: retrofit2.Call<List<com.example.appcitasmedicas.data.remote.LoginUserDto>>,
                    t: Throwable
                ) {
                    setLoading(false)
                    showError("No se pudo conectar al servidor")
                }
            })
    }

    private fun showError(msg: String) {
        binding.tvError.text = msg
        binding.tvError.visibility = View.VISIBLE
    }

//    private fun updateLoginButtonState() {
//        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
//        val pass = binding.etPassword.text?.toString().orEmpty()
//
//        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
//        val isPassValid = pass.length >= 6
//
//        binding.btnLogin.isEnabled = isEmailValid && isPassValid
//    }

    private fun updateLoginButtonState() {
        val usuario = binding.etEmail.text?.toString()?.trim().orEmpty()
        val pass = binding.etPassword.text?.toString().orEmpty()

        val isUserValid = usuario.length >= 3
        val isPassValid = pass.length >= 4   // o >= 6 si quieres estricto

        binding.btnLogin.isEnabled = isUserValid && isPassValid
    }

//    private fun validateInputs(): Boolean {
//        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
//        val pass = binding.etPassword.text?.toString().orEmpty()
//
//        binding.tilEmail.error = null
//        binding.tilPassword.error = null
//        binding.tvError.visibility = View.GONE
//
//        var ok = true
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            binding.tilEmail.error = "Ingrese un correo válido"
//            ok = false
//        }
//
//        if (pass.length < 6) {
//            binding.tilPassword.error = "Mínimo 6 caracteres"
//            ok = false
//        }
//        return ok
//    }

    private fun validateInputs(): Boolean {
        val usuario = binding.etEmail.text?.toString()?.trim().orEmpty()
        val pass = binding.etPassword.text?.toString().orEmpty()

        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tvError.visibility = View.GONE

        var ok = true

        if (usuario.length < 3) {
            binding.tilEmail.error = "Ingrese usuario válido"
            ok = false
        }

        if (pass.length < 4) {
            binding.tilPassword.error = "Clave inválida"
            ok = false
        }

        return ok
    }

    private fun goToServices() {
        val intent = Intent(this, ServicesActivity::class.java)
        // Esto limpia el stack para que no regreses al login con "Back"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading && validateForEnable()
        binding.btnGoRegister.isEnabled = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
    }

    private fun validateForEnable(): Boolean {
        val usuario = binding.etEmail.text?.toString()?.trim().orEmpty()
        val pass = binding.etPassword.text?.toString().orEmpty()
        return usuario.length >= 3 && pass.length >= 4
    }

//    private fun validateForEnable(): Boolean {
//        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
//        val pass = binding.etPassword.text?.toString().orEmpty()
//        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length >= 6
//    }

}