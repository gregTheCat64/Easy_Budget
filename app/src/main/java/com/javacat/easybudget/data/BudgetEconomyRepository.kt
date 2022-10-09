package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javacat.easybudget.domain.models.BudgetEconomy
import com.javacat.easybudget.domain.models.BudgetItem
import java.util.*
import kotlin.reflect.typeOf

class BudgetEconomyRepository(private val context: Context) {

    private val filename = "budgetEconomy.json"

    private var startDate: Calendar? = null
    private var expensesMonthlySum: Int = 0
    private var incomesMonthlySum: Int = 0
    var budgetEconomy = BudgetEconomy.newInstance()

//    val json =
//        """{"STARTDATE": "$startDate", "EXPENSES_SUM": "$expensesMonthlySum", "INCOMES_SUM" : "$incomesMonthlySum"}"""
    val gson = Gson()
    val type = object : TypeToken<BudgetEconomy>() {}.type
    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                budgetEconomy = gson.fromJson(it, type)
                Log.i("SAVESTART", "${budgetEconomy.startDate} in repo")


            }
        } else sync()
    }

    fun getBudgetEconomyObject():BudgetEconomy{
        return budgetEconomy
    }

    fun save(_budgetEconomy: BudgetEconomy){
        budgetEconomy = _budgetEconomy
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(budgetEconomy))
            Log.i("MyLog", "sync")
        }
        Log.i("MyLog", "$budgetEconomy in sync")
    }
}
