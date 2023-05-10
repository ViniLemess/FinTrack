package org.fundatec.vinilemess.tcc.fintrackapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import org.fundatec.vinilemess.tcc.fintrackapp.R
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ActivityTransactionRegistryBinding
import org.fundatec.vinilemess.tcc.fintrackapp.data.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.TransactionRegistryViewModel
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.TransactionState

class TransactionRegistryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionRegistryBinding
    private lateinit var viewModel: TransactionRegistryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTransactionRegistryBinding.inflate(layoutInflater)
        viewModel = TransactionRegistryViewModel()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            viewModel.saveTransaction(
                getCheckedType(),
                binding.etAmount.text.toString(),
                binding.etDescription.text.toString()
            )
        }

        viewModel.viewState.observe(this) { state ->
            when(state) {
                TransactionState.ValidTransaction -> showToast(getString(R.string.successfully_registered_transaction_msg))
                TransactionState.UnknownType -> showToast(getString(R.string.unknown_operation_msg))
                TransactionState.EmptyTransaction -> showToast(getString(R.string.empty_transaction_msg))
            }
        }

        binding.backArrow.setOnClickListener {
            finish()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getCheckedType(): TransactionOperation {
        if (binding.rdExpense.isChecked) {
            return TransactionOperation.EXPENSE
        } else if (binding.rdIncome.isChecked) {
            return TransactionOperation.INCOME
        }
        return TransactionOperation.UNKNOWN
    }
}