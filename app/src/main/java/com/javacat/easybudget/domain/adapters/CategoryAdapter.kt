package com.javacat.easybudget.domain.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.javacat.easybudget.R
import com.javacat.easybudget.databinding.CategoryItemCardBinding
import com.javacat.easybudget.domain.models.Category




class CategoryAdapter(
    private val categList: List<Category>, val listener: Listener, val context: Context
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    var selectedPosition = -1
    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CategoryItemCardBinding.bind(itemView)

        fun bind(category: Category, listener: Listener) {
            binding.apply {
                categoryItemText.text = category.name
                //categoryItem.setCompoundDrawablesWithIntrinsicBounds(category.pic, 0, 0, 0)
                categoryItemImage.setImageResource(category.pic)
            }

            when (selectedPosition) {
                -1 -> {
                    binding.categoryItemImage.setColorFilter(ContextCompat.getColor(context,R.color.secondary_text))
                }
                bindingAdapterPosition -> {
                    binding.categoryItemImage.setColorFilter(ContextCompat.getColor(context,R.color.accent_orange))
                }
                else -> binding.categoryItemImage.setColorFilter(ContextCompat.getColor(context,R.color.secondary_text))
            }


            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    binding.categoryItemImage.setColorFilter(ContextCompat.getColor(context,R.color.accent_orange))
                    listener.onClick(category)
                    if (selectedPosition != bindingAdapterPosition) {
                        notifyItemChanged(selectedPosition)
                        selectedPosition = bindingAdapterPosition

                        Log.i("MYADAP", "selected: $selectedPosition")
                    }
                }
            })
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






