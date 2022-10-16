package com.javacat.easybudget.domain.adapters

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javacat.easybudget.R
import com.javacat.easybudget.databinding.BudgetItemCardBinding
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Type
import java.text.DateFormat
import java.util.Calendar

class MainAdapter(private val listener: OnListener,val context: Context
): ListAdapter<BudgetItem, MainAdapter.BudgetItemViewHolder>(BudgetDiffCallback()) {

    inner class BudgetItemViewHolder(
        private val binding: BudgetItemCardBinding,
        private val listener: OnListener
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(budgetItem: BudgetItem){
            //val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG)
            val dateFormatter = DateFormat.getTimeInstance(DateFormat.SHORT)
            val date = budgetItem.date
            val budgetItemCost = budgetItem.cost
            binding.apply {
                budgetItemName.text = budgetItem.name
                budgetItemPic.setImageResource(budgetItem.category.pic)

                if (budgetItem.category.type == Type.EXPENSES){
                    budgetItemPrice.text = "-$budgetItemCost, руб."
                    budgetItemPrice.setTextColor(ContextCompat.getColor(context, R.color.accent_orange))
                } else {
                    budgetItemPrice.text = "+${budgetItem.cost}, руб."
                    budgetItemPrice.setTextColor(ContextCompat.getColor(context, R.color.dark_green))
                }
                budgetItemDate.text = dateFormatter.format(date.time)
                deleteBtn.setOnClickListener {
                    listener.onRemove(budgetItem)
                }
            }
        }
    }


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
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim))
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

