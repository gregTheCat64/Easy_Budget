package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CurrentMonthRepository(private val context: Context) {
    private var currentMonth = 0
    private val filename = "currentMonth.txt"
    private val currentMonthData = MutableLiveData(currentMonth)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                currentMonth = it.readText().toInt()
                this.currentMonthData.value = currentMonth
            }
        } else sync()
    }

    fun getCurrentMonth(): LiveData<Int> = currentMonthData

    fun saveCurrentMonth(currentMonthValue: Int){
        this.currentMonth = currentMonthValue
        currentMonthData.value = currentMonth
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(this.currentMonth.toString())

        }
    }
}