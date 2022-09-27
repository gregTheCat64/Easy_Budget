package com.javacat.easybudget.domain.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javacat.easybudget.databinding.BudgetItemCardBinding
import com.javacat.easybudget.domain.models.BudgetItem

class MainAdapter(private val listener: OnListener
): ListAdapter<BudgetItem, BudgetItemViewHolder>(BudgetDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetItemViewHolder {
        val binding = BudgetItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetItemViewHolder(
            binding= binding,
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: BudgetItemViewHolder, position: Int) {
        val budgetItem = getItem(position)
        holder.bind(budgetItem)
    }
}

interface OnListener{
    fun onRemove(budgetItem: BudgetItem)
}

class BudgetDiffCallback: DiffUtil.ItemCallback<BudgetItem>(){
    override fun areItemsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
        return oldItem == newItem
    }
}

class BudgetItemViewHolder(
    private val binding: BudgetItemCardBinding,
    private val listener: OnListener
): RecyclerView.ViewHolder(binding.root) {
    fun bind(budgetItem: BudgetItem){
        binding.apply {
            budgetItemName.text = budgetItem.name
            budgetItemPic.setImageResource(budgetItem.category.pic)
            budgetItemPrice.text = budgetItem.cost.toString()
            budgetItemDate.text = budgetItem.date.toString()
            deleteBtn.setOnClickListener {
                listener.onRemove(budgetItem)

            }
        }
    }
}