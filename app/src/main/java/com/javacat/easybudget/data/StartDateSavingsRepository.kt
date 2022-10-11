package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

class StartDateSavingsRepository(private val context: Context) {

    private var startDate = LocalDate.of(2022,10, 10)
    private val filename = "startDateSavings.txt"
    //private val startDateData = MutableLiveData(startDate)

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                val stringDate = it.readText()
                startDate = LocalDate.parse(stringDate)
               // this.startDateData.value = startDate
            }
        } else sync()
    }


    fun getStartDate(): LocalDate = startDate


    fun saveStartDate(startDate: LocalDate) {
        this.startDate = startDate
       // startDateData.value = startDate
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(this.startDate.toString())
            Log.d("STARTDATE", this.startDate.toString())
        }
    }
}