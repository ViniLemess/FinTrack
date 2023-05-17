package org.fundatec.vinilemess.tcc.fintrackapp.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.fundatec.vinilemess.tcc.fintrackapp.adapter.TransactionAdapter
import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ActivityTransactionsBinding
import org.fundatec.vinilemess.tcc.fintrackapp.showToast
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.TransactionsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionsActivity : AppCompatActivity(), TransactionAdapter.Listener,
    DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityTransactionsBinding
    private val adapter: TransactionAdapter by lazy {
        TransactionAdapter(this)
    }
    private lateinit var viewModel: TransactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = TransactionsViewModel(this)

        Log.e("====> Tela listagem", "passei aqui")

        viewModel.listTransactions.observe(this) {
            binding.rcList.adapter = adapter
            if (it != null) {
                adapter.addNewList(it)
            }
            Log.e("====> objeto buscado", it.toString())
        }

        binding.plusButton.setOnClickListener {
            val intent = Intent(this, TransactionRegistryActivity::class.java)
            startActivity(intent)
        }

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.deleteAll.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            if (selectedItems.isEmpty()) {
                showToast("No transaction selected", this)
                return@setOnClickListener
            }
            viewModel.deleteTransactions(selectedItems)
            recreate()
        }

        binding.listingDateButton.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(supportFragmentManager, "datePicker")
        }
    }

    override fun onItemSelected(transaction: Transaction) {
        Log.i("Selected Transaction", transaction.id)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getTransactions(null)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = dateFormat.format(calendar.time)

        viewModel.getTransactions(formattedDate)
    }
}

