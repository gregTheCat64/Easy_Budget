package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class BalanceRepositoryImpl(private val context: Context):com.javacat.easybudget.domain.BalanceRepository {

    private var startBudget = 0
    private val filename = "currentBudget.txt"
    private val startBudgetData = MutableLiveData(startBudget)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                startBudget = it.readText().toInt()
                this.startBudgetData.value = startBudget
            }
        } else sync()
    }


    override fun getStartBudget(): LiveData<Int> = startBudgetData


    override fun saveStartBudget(budgetValue: Int) {
       this.startBudget = budgetValue
        startBudgetData.value = startBudget
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(this.startBudget.toString())
            Log.d("BUDGET", this.startBudget.toString())
        }
    }
}