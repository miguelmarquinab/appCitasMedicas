package com.example.appcitasmedicas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
            if (validateInputs()) {
                fakeLogin() // Por ahora simulado (luego conectamos API)
            }
        }

        // 6) Click en "Registrarme" (por ahora solo mensaje)
        binding.btnGoRegister.setOnClickListener {
            binding.tvError.text = "Registro aún no implementado (siguiente paso)."
            binding.tvError.visibility = View.VISIBLE
        }
    }

    private fun updateLoginButtonState() {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val pass = binding.etPassword.text?.toString().orEmpty()

        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPassValid = pass.length >= 6

        binding.btnLogin.isEnabled = isEmailValid && isPassValid
    }

    private fun validateInputs(): Boolean {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val pass = binding.etPassword.text?.toString().orEmpty()

        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tvError.visibility = View.GONE

        var ok = true

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Ingrese un correo válido"
            ok = false
        }

        if (pass.length < 6) {
            binding.tilPassword.error = "Mínimo 6 caracteres"
            ok = false
        }

        return ok
    }

    private fun fakeLogin() {
        // UX: mostramos loading y bloqueamos el botón para evitar doble clic
        setLoading(true)

        // Simulación rápida (sin hilos todavía para mantenerlo simple)
        binding.root.postDelayed({
            setLoading(false)

            // ✅ Login ok (por ahora siempre)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 900)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading && validateForEnable()
        binding.btnGoRegister.isEnabled = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
    }

    private fun validateForEnable(): Boolean {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val pass = binding.etPassword.text?.toString().orEmpty()
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length >= 6
    }
}