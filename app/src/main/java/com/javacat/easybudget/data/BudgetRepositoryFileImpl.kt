package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.javacat.easybudget.domain.BudgetRepository
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class BudgetRepositoryFileImpl(
    private val context: Context
) : BudgetRepository {
    private val gson = Gson()

    private val type = TypeToken.getParameterized(List::class.java, BudgetItem::class.java).type
    private val filename = "budgetItems.json"
    private var budgetItems = emptyList<BudgetItem>()

    private val data = MutableLiveData(budgetItems)
    private val expenseDataByMonth = MutableLiveData<List<BudgetItem>>()
    private val incomeDataByMonth = MutableLiveData<List<BudgetItem>>()
    private val expenseDataByDay = MutableLiveData<List<BudgetItem>>()
    private val incomeDataByDay = MutableLiveData<List<BudgetItem>>()
    private val commonDataByDay = MutableLiveData<List<BudgetItem>>()
    private val daysListData = MutableLiveData<List<BudgetItem>>()

    var currentDate = MutableLiveData<Calendar>()

    private var lastId: Long? = 0L

    init {
        Log.i("REPO", "init")
        val file = context.filesDir.resolve(filename)
        if (file.exists()){
            context.openFileInput(filename).bufferedReader().use{
                budgetItems = gson.fromJson(it, type)
                data.value = budgetItems

                updateIncomesByMonth()
                updateExpensesByMonth()
                updateExpensesByDay()
                updateIncomesByDay()
                updateDayList()

            }
        }else sync()
    }

    override fun getAll(): LiveData<List<BudgetItem>>{
        Log.i("LIFE", "repo getAll")
        return data
    }

    override fun getExpensesByMonth(): LiveData<List<BudgetItem>> {
        Log.i("REPO", "getExpByMonth")
        return expenseDataByMonth
    }

    override fun getIncomesByMonth(): LiveData<List<BudgetItem>> = incomeDataByMonth

    override fun getDayList(): LiveData<List<BudgetItem>> {
        return daysListData
    }

    private fun updateExpensesByMonth(){
        Log.i("REPO", "updateExpByMonth")
        val expenses = budgetItems.filter { it.category.type == Type.EXPENSES
                && it.date.get(Calendar.MONTH) == currentDate.value?.get(Calendar.MONTH)
                && it.date.get(Calendar.YEAR) == currentDate.value?.get(Calendar.YEAR)}
        Log.i("REPO", "budgetITems in updateByMonth: $expenses")
        Log.i("REPO", "currentDate in updateByMonth: ${currentDate.value}")

        expenseDataByMonth.value = expenses
    }

    private fun updateIncomesByMonth(){
        val incomes= budgetItems.filter { it.category.type == Type.INCOMES
                && it.date.get(Calendar.MONTH) == currentDate.value?.get(Calendar.MONTH)
                && it.date.get(Calendar.YEAR) == currentDate.value?.get(Calendar.YEAR)}
        incomeDataByMonth.value = incomes
    }

    private fun updateDayList(){
        val days = budgetItems.filter {it.date.get(Calendar.MONTH) == currentDate.value?.get(Calendar.MONTH)&&
                it.date.get(Calendar.YEAR) == currentDate.value?.get(Calendar.YEAR)  }
        daysListData.value = days
    }



    override fun setCurrentDay(date: Calendar) {
        Log.i("REPO", "setCurrentDay")
        currentDate.value = date
        updateExpensesByDay()
        updateIncomesByDay()
        updateExpensesByMonth()
        updateIncomesByMonth()
        updateCommonByDay()
        updateDayList()
    }

    override fun getCurrentDay(): Calendar?{
        val date = currentDate.value
        return date
    }

    override fun getIncomesByDay(): LiveData<List<BudgetItem>> = incomeDataByDay


    override fun getExpensesByDay(): LiveData<List<BudgetItem>> {
        Log.i("REPO", "getExpByDay")
        updateExpensesByDay()
        return expenseDataByDay
    }

    override fun getCommonByDay(): LiveData<List<BudgetItem>> = commonDataByDay

    private fun updateIncomesByDay(){
        Log.i("REPO", "updateIncByDay")
        Log.i("REPO", "budgetITems in updateByDay: $budgetItems")
        val incomes = budgetItems.filter { it.category.type == Type.INCOMES
                && it.date.get(Calendar.DATE) == currentDate.value?.get(Calendar.DATE)
                && it.date.get(Calendar.MONTH) == currentDate.value?.get(Calendar.MONTH)
                && it.date.get(Calendar.YEAR) == currentDate.value?.get(Calendar.YEAR)
        }
        Log.i("REPO", "currentDate in updateByDay: ${currentDate.value}")
        incomeDataByDay.value = incomes
    }

    private fun updateExpensesByDay(){
        Log.i("LIFE", "updateExpByDay")
        val expenses = budgetItems.filter { it.category.type == Type.EXPENSES
                && it.date.get(Calendar.DATE) == currentDate.value?.get(Calendar.DATE)
                && it.date.get(Calendar.MONTH) == currentDate.value?.get(Calendar.MONTH)
                && it.date.get(Calendar.YEAR) == currentDate.value?.get(Calendar.YEAR)
        }
        Log.i("TIME", "budgetDate: ${currentDate.value}")
        Log.i("TIME", "filterDate: ${expenses}")
        expenseDataByDay.value = expenses
    }

    private fun updateCommonByDay(){
        val commons = budgetItems.filter { it.date.get(Calendar.DATE) == currentDate.value?.get(Calendar.DATE)
                && it.date.get(Calendar.MONTH) == currentDate.value?.get(Calendar.MONTH)
                && it.date.get(Calendar.YEAR) == currentDate.value?.get(Calendar.YEAR) }
        commonDataByDay.value = commons
    }




    override fun removeById(id: Long) {
        budgetItems = budgetItems.filter { it.id != id }
        data.value = budgetItems
        Log.i("LIFE", "repo_remove")
//        updateExpensesByMonth()
//        updateIncomesByMonth()
        updateCommonByDay()
        updateDayList()
        sync()

    }

    override fun save(budgetItem: BudgetItem) {
        Log.i("LIFE", "in repoSave: ${budgetItem.date}")
        Log.i("LIFE", "repo_save")
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

    class LocalDateAdapter : JsonSerializer<LocalDate?> {
        override fun serialize(
            src: LocalDate?,
            typeOfSrc: java.lang.reflect.Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            //val firstApiFormat = DateTimeFormatter.ofPattern("{\"day\":dd,\"month\":mm,\"year\":yyyy}")
            return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_DATE)) // "yyyy-mm-dd"
            //return JsonPrimitive(src?.format(firstApiFormat)) // "yyyy-mm-dd"
        }
       // "date":{"day":9,"month":10,"year":2022}
        //("\"day\":dd,\"month\":mm,\"year\":yyyy")
    }
}