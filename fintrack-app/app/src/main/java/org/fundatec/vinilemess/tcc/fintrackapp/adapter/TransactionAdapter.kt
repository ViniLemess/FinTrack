package org.fundatec.vinilemess.tcc.fintrackapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fundatec.vinilemess.tcc.fintrackapp.R
import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.data.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrackapp.databinding.ItemListBinding
import org.fundatec.vinilemess.tcc.fintrackapp.toCurrency

private const val GREEN_BLACK = "#000F00"
private const val RED_BLACK = "#0F0000"

class TransactionAdapter(private val listener: Listener) :
    RecyclerView.Adapter<TransactionViewHolder>() {

    interface Listener {
        fun onItemSelected(transaction: Transaction)
    }

    private val listItem: MutableList<Transaction> = mutableListOf()

    fun getSelectedItems(): List<Transaction> = listItem.filter { it.isSelected }

    fun clearSelections() {
        listItem.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

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
                "+${transaction.amount.toCurrency()}".also { binding.transactionPrice.text = it }
                binding.transactionImg.setImageResource(R.drawable.income_icon)
                updateCardBackgroundColor(transaction, GREEN_BLACK)
            }
            TransactionOperation.EXPENSE -> {
                transaction.amount.toCurrency().also { binding.transactionPrice.text = it }
                binding.transactionImg.setImageResource(R.drawable.expense_icon)
                updateCardBackgroundColor(transaction, RED_BLACK)
            }
            else -> {}
        }
        binding.root.setOnLongClickListener {
            transaction.isSelected = !transaction.isSelected

            when (transaction.transactionOperation) {
                TransactionOperation.INCOME -> updateCardBackgroundColor(transaction, GREEN_BLACK)
                TransactionOperation.EXPENSE -> updateCardBackgroundColor(transaction, RED_BLACK)
                else -> {}
            }
            listener.onItemSelected(transaction)
            true
        }
    }

    private fun updateCardBackgroundColor(transaction: Transaction, newColor: String) {
        binding.transactionCard.setCardBackgroundColor(if (transaction.isSelected) Color.DKGRAY else Color.parseColor(newColor))
    }
}
