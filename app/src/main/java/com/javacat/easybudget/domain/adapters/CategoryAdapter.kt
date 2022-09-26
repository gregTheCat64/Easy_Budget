package com.javacat.easybudget.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javacat.easybudget.R
import com.javacat.easybudget.databinding.CategoryItemCardBinding
import com.javacat.easybudget.domain.models.Category


class CategoryAdapter(
    private val categList: List<Category>, val listener: Listener
 ) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CategoryItemCardBinding.bind(itemView)
        fun bind(category: Category, listener: Listener) {
            binding.apply {
                categoryItem.text = category.name
                categoryItem.setCompoundDrawablesWithIntrinsicBounds(category.pic, 0, 0, 0)
                categoryItem.setOnClickListener {
                    listener.onClick(category)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_card, parent, false)
        return CategoryHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(categList[position], listener)
    }

    override fun getItemCount(): Int {
        return categList.size
    }

    interface Listener {
        fun onClick(category: Category)
    }
}




