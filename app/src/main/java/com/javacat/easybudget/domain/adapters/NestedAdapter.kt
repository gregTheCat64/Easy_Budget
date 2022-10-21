package com.javacat.easybudget.domain.adapters

import android.content.Context
import com.javacat.easybudget.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.javacat.easybudget.databinding.BudgetItemCardBinding
import com.javacat.easybudget.databinding.ItemsListBinding
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Type
import java.text.DateFormat

interface OnItemListener {
    fun onRemove(budgetItem: BudgetItem)
}

class NestedAdapter(
    private val listener: OnItemListener, private val mList: List<BudgetItem>, val context: Context
) : ListAdapter<BudgetItem, NestedAdapter.NestedViewHolder>(BudgetDiffCallback()) {

    inner class NestedViewHolder(
        private val binding: BudgetItemCardBinding,
        private val listener: OnItemListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(budgetItem: BudgetItem){
            val price: String
            val color: Int
            val dateFormatter = DateFormat.getTimeInstance(DateFormat.SHORT)
            val date = budgetItem.date

            binding.apply {
                budgetItemName.text = budgetItem.name
                budgetItemPic.setImageResource(budgetItem.category.pic)

                if (budgetItem.category.type == Type.EXPENSES) {
                    price = "-${budgetItem.cost}, руб."
                    color = ContextCompat.getColor(context, R.color.accent_orange)
                } else {
                    price = "+${budgetItem.cost}, руб."
                    color = ContextCompat.getColor(context, R.color.dark_green)
                }
                budgetItemPrice.text = price
                budgetItemPrice.setTextColor(color)
                budgetItemPic.setImageResource(budgetItem.category.pic)
                budgetItemDate.text = dateFormatter.format(date.time)
                deleteBtn.setOnClickListener {
                    listener.onRemove(budgetItem)
                    itemView.setAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.anim))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedViewHolder {
       val binding = BudgetItemCardBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return NestedViewHolder(
            binding, listener
        )
    }

    override fun onBindViewHolder(holder: NestedViewHolder, position: Int) {
        val budgetItem = mList[position]
        holder.bind(budgetItem)
        //holder.itemView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim))

    }

    override fun getItemCount(): Int {
        return mList.size
    }


}



