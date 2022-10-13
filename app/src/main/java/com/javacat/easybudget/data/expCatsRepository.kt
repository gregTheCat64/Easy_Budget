package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Category

class expCatsRepository(
    private val context: Context
) {
    private val gson = Gson()

    private val type = TypeToken.getParameterized(List::class.java, Category::class.java).type
    private val filename = "expCatsRepo.json"
    private var expCatList = emptyList<BudgetItem>()

    private val data = MutableLiveData(expCatList)

    private var lastId = 0

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()){
            context.openFileInput(filename).bufferedReader().use{
                expCatList = gson.fromJson(it, type)
                data.value = expCatList
            }
        }else sync()
    }


    private fun sync(){
        Log.i("LIFE", "sync")
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(expCatList))
        }
    }
}