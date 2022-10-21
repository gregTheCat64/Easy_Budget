package com.javacat.easybudget.data

import android.content.Context

class BudgetEconomyRepository(private val context: Context) {

//    private val filename = "budgetEconomy.json"
//
//      var budgetEconomy = BudgetEconomy.newInstance()
//
////    val json =
////        """{"STARTDATE": "$startDate", "EXPENSES_SUM": "$expensesMonthlySum", "INCOMES_SUM" : "$incomesMonthlySum"}"""
//    val gson = Gson()
//    val type = object : TypeToken<BudgetEconomy>() {}.type
//    init {
//        val file = context.filesDir.resolve(filename)
//        if (file.exists()) {
//            context.openFileInput(filename).bufferedReader().use {
//                budgetEconomy = gson.fromJson(it, type)
//                Log.i("SAVESTART", "${budgetEconomy.startDate} in repo")
//
//
//            }
//        } else sync()
//    }
//
//    fun getBudgetEconomyObject():BudgetEconomy{
//        return budgetEconomy
//    }
//
//    fun save(_budgetEconomy: BudgetEconomy){
//        budgetEconomy = _budgetEconomy
//        sync()
//    }
//
//    private fun sync() {
//        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
//            it.write(gson.toJson(budgetEconomy))
//            Log.i("MyLog", "sync")
//        }
//        Log.i("MyLog", "$budgetEconomy in sync")
//    }
}
