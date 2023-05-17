package org.fundatec.vinilemess.tcc.fintrackapp.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ActivityHomeBinding
import org.fundatec.vinilemess.tcc.fintrackapp.getCurrentDateAsString
import org.fundatec.vinilemess.tcc.fintrackapp.toCurrency
import org.fundatec.vinilemess.tcc.fintrackapp.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHomeBinding.inflate(layoutInflater).run {
            binding = this
            setContentView(root)
        }
        viewModel = HomeViewModel(this)

        viewModel.showUsername.observe(this) { username ->
            "Hello $username!".also { binding.tvHelloUser.text = it }
        }

        viewModel.showBalance.observe(this) {
            binding.tvAccountBalance.text = it.amount.toCurrency()
            binding.balanceFetchDate.text = it.date
        }

        viewModel.showBalanceProjection.observe(this) {
            binding.projectionAccountBalance.text = it.amount.toCurrency()
            binding.projectionBalanceFetchDate.text = it.date
        }

        binding.transactionsButton.setOnClickListener {
            val intent = Intent(this, TransactionsActivity::class.java)
            startActivity(intent)
        }

        binding.transactionRgButton.setOnClickListener {
            val intent = Intent(this, TransactionRegistryActivity::class.java)
            startActivity(intent)
        }

        binding.tvProjectionBalanceButton.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(supportFragmentManager, "datePicker")
        }

        binding.imgvExit.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUsername()
        viewModel.getBalance()
        binding.projectionAccountBalance.text = 0.0.toCurrency()
        binding.projectionBalanceFetchDate.text = getCurrentDateAsString()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = dateFormat.format(calendar.time)

        viewModel.getBalanceProjection(formattedDate)
    }
}
