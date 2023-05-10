package org.fundatec.vinilemess.tcc.fintrackapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ActivityTransactionsBinding
import org.fundatec.vinilemess.tcc.fintrackapp.adapter.TransactionAdapter
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.TransactionsViewModel

class TransactionsActivity : AppCompatActivity(), TransactionAdapter.Listener {
    private lateinit var binding: ActivityTransactionsBinding
    private val adapter: TransactionAdapter by lazy {
        TransactionAdapter(this)
    }
    private lateinit var viewModel: TransactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = TransactionsViewModel()

        Log.e("====> Tela listagem", "passei aqui")

        viewModel.listTransactions.observe(this) {
                binding.rcList.adapter = adapter
                if (it != null) {
                    adapter.addNewList(it)
                }
                Log.e("====> objeto buscado", it.toString())
        }

        binding.plusButton.setOnClickListener {
            val intent  = Intent(this, TransactionRegistryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(text: String) {
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.findTransactions()
    }
}

