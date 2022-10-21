package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javacat.easybudget.domain.RegularRepository
import com.javacat.easybudget.domain.models.BudgetItem

class RegularRepositoryImpl(
    private val context: Context
):RegularRepository {
    private val gson = Gson()

    private val type = TypeToken.getParameterized(List::class.java, BudgetItem::class.java).type
    private val filename = "regularBudgetItems.json"
    private var budgetItems = emptyList<BudgetItem>()

    private val data = MutableLiveData(budgetItems)

    private var lastId: Long? = 0L

    init {

        val file = context.filesDir.resolve(filename)
        if (file.exists()){
            context.openFileInput(filename).bufferedReader().use{
                budgetItems = gson.fromJson(it, type)
                data.value = budgetItems
            }
        }else sync()
    }

    override fun getAll(): LiveData<List<BudgetItem>> {
        return data
    }

    override fun removeById(id: Long) {
        budgetItems = budgetItems.filter { it.id != id }
        data.value = budgetItems
        sync()
    }

    override fun save(budgetItem: BudgetItem) {
        lastId = budgetItems.maxOfOrNull { it.id }
        if (budgetItem.id == 0L){
            budgetItems = listOf(budgetItem.copy(
                id = lastId?.plus(1) ?:1
            )) + budgetItems
            data.value = budgetItems
            sync()
            return
        }

        budgetItems = budgetItems.map {
            if (it.id != budgetItem.id) it else it.copy(name = budgetItem.name, cost = budgetItem.cost)
        }
        data.value = budgetItems
        sync()
    }

    private fun sync(){
        Log.i("LIFE", "sync")
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(budgetItems))
        }
    }
}