package org.fundatec.vinilemess.tcc.fintrackapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ActivityHomeBinding
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHomeBinding.inflate(layoutInflater).run {
            binding = this
            setContentView(root)
        }
        viewModel = HomeViewModel(this)

        viewModel.showBalance.observe(this) {
            binding.tvAccountBalance.text = it
        }

        viewModel.showUsername.observe(this) {
            binding.tvHelloUser.text = "Hello $it!"
        }

        binding.transactionButton.setOnClickListener {
            val intent = Intent(this, TransactionsActivity::class.java)
            startActivity(intent)
        }

        binding.transactionRgButton.setOnClickListener {
            val intent = Intent(this, TransactionRegistryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUsername()
        viewModel.getBalance()
    }
}
