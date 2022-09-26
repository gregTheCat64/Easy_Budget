package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javacat.easybudget.domain.BudgetItemRepository
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Type

class BudgetItemRepositoryFileImpl(
    private val context: Context
) : BudgetItemRepository {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, BudgetItem::class.java).type
    private val filename = "budgetItems.json"
    private var budgetItems = emptyList<BudgetItem>()
    private val data = MutableLiveData(budgetItems)
    private val expenseData = MutableLiveData(budgetItems)
    private val incomeData = MutableLiveData(budgetItems)
    private lateinit var expenses: List<BudgetItem>
    private lateinit var incomes: List<BudgetItem>
    private var lastId: Long? = 0L



    //private lateinit var incomes: List<BudgetItem>

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()){
            context.openFileInput(filename).bufferedReader().use{
                budgetItems = gson.fromJson(it, type)
                data.value = budgetItems
            }
        }else sync()
    }

    override fun getAll(): LiveData<List<BudgetItem>> = data

    override fun removeById(id: Long) {
        budgetItems = budgetItems.filter { it.id != id }
        data.value = budgetItems
        filterByCategories()
        expenseData.value = expenses
        incomeData.value = incomes
        Log.i("MyLog", "repoRemoving")
        Log.i("MyLog", "$budgetItems budgetItems in repo-remove")
        Log.i("MyLog", "$expenses expenses in repo-remove" )
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

    override fun getIncomes(): LiveData<List<BudgetItem>> {
        filterByCategories()
        incomeData.value = incomes
        return incomeData
    }

    override fun getExpenses(): LiveData<List<BudgetItem>> {
        filterByCategories()
        expenseData.value = expenses
        Log.i("MyLog", "$expenses in RepoGetExpenses")
        return expenseData

    }

    private fun filterByCategories() {
        Log.i("MyLog", "$budgetItems in Filter")
        expenses = budgetItems.filter { it.category.type == Type.EXPENSES }
        incomes = budgetItems.filter { it.category.type==Type.INCOMES }
    }

    private fun sync(){
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(budgetItems))
            Log.i("MyLog", "sync")
        }
        Log.i("MyLog", "$budgetItems in sync")
    }
}