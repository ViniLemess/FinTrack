package org.fundatec.vinilemess.tcc.fintrackapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ActivitySplashBinding
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.LoginViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = LoginViewModel(this)

        viewModel.shouldShowHome.observe(this) {
            if (!it) {
                Handler().postDelayed({
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 3000)
            } else {
                Handler().postDelayed({
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 3000)
            }
        }
    }

}