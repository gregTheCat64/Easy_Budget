package com.javacat.easybudget.domain.adapters

import android.content.Context
import com.javacat.easybudget.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javacat.easybudget.domain.models.BudgetDataModel
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Weeks
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar


class ItemAdapter(mList: List<BudgetDataModel>, val context: Context, private val listener: OnItemListener) :
    ListAdapter<BudgetItem, ItemAdapter.ItemViewHolder>(BudgetDiffCallback()) {
    private val mList: List<BudgetDataModel>
    private var list: List<BudgetItem> = ArrayList()

    init {
        this.mList = mList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model: BudgetDataModel = mList[position]
        list = model.nestedList
        val dateFormatter2 = SimpleDateFormat("EEEE")
        //val day = dateFormatter.format(model.day)
        val date = list[0].date.get(Calendar.DATE)
        val dayOfWeek = list[0].date.get(Calendar.DAY_OF_WEEK)
        holder.mTextView.text = date.toString()
        holder.dayOfWeek.text =  Weeks.weeks[dayOfWeek-1]

        //TODO разобраться с форматом
        val adapter = NestedAdapter(listener, list,context)
        holder.nestedRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.nestedRecyclerView.setHasFixedSize(true)
        holder.nestedRecyclerView.adapter = adapter

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout
        val mTextView: TextView
        val nestedRecyclerView: RecyclerView
        val dayOfWeek: TextView

        init {
            linearLayout = itemView.findViewById(R.id.ll)
            mTextView = itemView.findViewById(R.id.dayTextView)
            nestedRecyclerView = itemView.findViewById(R.id.childRecView)
            dayOfWeek = itemView.findViewById(R.id.dayOfWeek)
        }
    }
}

//class ItemsDiffCallback: DiffUtil.ItemCallback<BudgetDataModel>(){
//    override fun areItemsTheSame(oldItem: BudgetDataModel, newItem: BudgetDataModel): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: BudgetDataModel, newItem: BudgetDataModel): Boolean {
//        return oldItem == newItem
//    }
//}


