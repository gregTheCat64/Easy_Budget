package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javacat.easybudget.domain.BudgetRepository
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Type

class BudgetRepositoryFileImpl(
    private val context: Context
) : BudgetRepository {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, BudgetItem::class.java).type
    private val filename = "budgetItems.json"
    private var budgetItems = emptyList<BudgetItem>()
    private val data = MutableLiveData(budgetItems)
    val _expenseData = MutableLiveData(budgetItems)
    val expenseData:LiveData<List<BudgetItem>> = _expenseData
    val _incomeData = MutableLiveData<List<BudgetItem>>()
    val incomeData:LiveData<List<BudgetItem>> = _incomeData
    var currentMonth = MutableLiveData<Int>()



    private var lastId: Long? = 0L

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()){
            context.openFileInput(filename).bufferedReader().use{
                budgetItems = gson.fromJson(it, type)
                data.value = budgetItems
                updateIncomes()
                updateExpenses()

            }
        }else sync()
    }

    override fun getAll(): LiveData<List<BudgetItem>> = data

    override fun getExpenses(): LiveData<List<BudgetItem>> = expenseData

    override fun getIncomes(): LiveData<List<BudgetItem>> = incomeData

    override fun saveCurrentMonth(month: Int) {
        currentMonth.value = month
        updateExpenses()
        updateIncomes()
        Log.i("MyLog", "save ${currentMonth.value} in Repo")
    }

    fun updateExpenses(){
        val expenses = budgetItems.filter { it.category.type == Type.EXPENSES && it.date.time.month == currentMonth.value}
        Log.i("MyLog", "${currentMonth.value} in updateExpenses")
        _expenseData.value = expenses
    }

    fun updateIncomes(){
        val incomes= budgetItems.filter { it.category.type == Type.INCOMES && it.date.time.month == currentMonth.value}
        _incomeData.value = incomes
    }


    override fun removeById(id: Long) {
        budgetItems = budgetItems.filter { it.id != id }
        data.value = budgetItems
        updateExpenses()
        updateIncomes()
        Log.i("MyLog", "repoRemoving")
//        Log.i("MyLog", "$budgetItems budgetItems in repo-remove")
//        Log.i("MyLog", "$expenses expenses in repo-remove" )
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

//    override fun getIncomes(): LiveData<List<BudgetItem>> {
//        incomeData.value = incomes
//        return incomeData
//    }
//
//    override fun getExpenses(): LiveData<List<BudgetItem>> {
//        expenseData.value = expenses
//        Log.i("MyLog", "${expenses.size} in RepoGetExpenses")
//        return expenseData
//    }




    private fun sync(){
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(budgetItems))
            Log.i("MyLog", "sync")
        }
        Log.i("MyLog", "$budgetItems in sync")
    }
}