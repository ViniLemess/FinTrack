package org.fundatec.vinilemess.tcc.fintrackapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ActivityUserRegistryBinding
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.UserRegistryViewModel

class UserRegistryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserRegistryBinding
    private lateinit var viewModel: UserRegistryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUserRegistryBinding.inflate(layoutInflater).run {
            binding = this
            viewModel = UserRegistryViewModel()
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