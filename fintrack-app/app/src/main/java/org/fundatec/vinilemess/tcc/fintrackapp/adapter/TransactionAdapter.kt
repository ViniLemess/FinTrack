package org.fundatec.vinilemess.tcc.fintrackapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fundatec.vinilemess.tcc.fintrackapp.R
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ItemListBinding
import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.data.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrackapp.toCurrency
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class TransactionAdapter(private val listener: Listener) :
    RecyclerView.Adapter<TransactionViewHolder>() {

    interface Listener {
        fun onItemClick(text: String)
    }

    private val listItem: MutableList<Transaction> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = listItem[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
    fun addNewList(list: List<Transaction>) {
        listItem.clear()
        notifyItemRangeRemoved(0, listItem.size)
        listItem.addAll(list)
    }
}

class TransactionViewHolder(
    private val binding: ItemListBinding,
    private val listener: TransactionAdapter.Listener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(transaction: Transaction) {
        binding.transactionDate.text = transaction.date
        binding.transactionDescription.text = transaction.description

        when (transaction.transactionOperation) {
            TransactionOperation.INCOME -> {
                "+ ${transaction.amount.toCurrency()}".also { binding.transactionPrice.text = it }
                binding.transactionImg.setImageResource(R.drawable.income_icon)
                binding.transactionCard.setCardBackgroundColor(Color.parseColor("#000F00"))
            }
            TransactionOperation.EXPENSE -> {
                "- ${transaction.amount.toCurrency()}".also { binding.transactionPrice.text = it }
                binding.transactionImg.setImageResource(R.drawable.expense_icon)
                binding.transactionCard.setCardBackgroundColor(Color.parseColor("#0F0000"))
            }
            else -> {}
        }

        binding.root.setOnClickListener {
            listener.onItemClick(transaction.amount.toString())
        }
    }
}
