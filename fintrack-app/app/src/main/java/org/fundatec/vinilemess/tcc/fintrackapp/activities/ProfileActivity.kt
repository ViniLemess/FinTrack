package org.fundatec.vinilemess.tcc.fintrackapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ActivityProfileBinding
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityProfileBinding.inflate(layoutInflater).run {
            binding = this
            viewModel = ProfileViewModel()
            setContentView(root)
        }

        binding.btSave.setOnClickListener {
            viewModel.saveUser(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
            finish()
        }
    }
}