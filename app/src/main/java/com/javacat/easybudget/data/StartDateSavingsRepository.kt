package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.util.Calendar

class StartDateSavingsRepository(private val context: Context) {

    //private var startDate = LocalDate.of(2022,10, 10)
    private var startDate = Calendar.getInstance()
    private val filename = "startDateSavings.txt"
    val type = object : TypeToken<Calendar>() {}.type
    val gson = Gson()
    private val _startDate = MutableLiveData(startDate)

    init {

        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                startDate = gson.fromJson(it,type)
                this._startDate.value = startDate
            }
        } else sync()
    }


    fun getStartDate(): LiveData<Calendar> {
        Log.i("MY_VM", "in repo ${startDate.time}")
        return _startDate
    }


    fun saveStartDate(startDate: Calendar) {
        this.startDate = startDate
        _startDate.value = startDate
       // startDateData.value = startDate
        sync()
    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(startDate))
            Log.d("STARTDATE", this.startDate.toString())
        }
    }
}