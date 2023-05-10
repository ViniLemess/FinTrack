package org.fundatec.vinilemess.tcc.fintrackapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ActivityLoginBinding
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = LoginViewModel()

        val view = binding.root
        setContentView(view)

        binding.btLogin.setOnClickListener {
            viewModel.login(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }

        viewModel.shouldShowHome.observe(this) { shouldOpen ->
            if (shouldOpen) {
                val intent  = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        viewModel.shouldShowError.observe(this) { shouldShow ->
            if (shouldShow){
                Toast.makeText(
                    this,
                    "Login Invalido",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.registryLink.setOnClickListener {
            val intent  = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}