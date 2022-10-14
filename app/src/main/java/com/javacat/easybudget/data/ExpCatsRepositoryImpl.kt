package com.javacat.easybudget.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javacat.easybudget.domain.ExpCatsRepository
import com.javacat.easybudget.domain.models.Category
import kotlin.math.exp

class ExpCatsRepositoryImpl(private val context: Context):ExpCatsRepository {
    private val gson = Gson()
    val defaultExpList = ExpenseCategData().getAll()
    var expenses = emptyList<Category>()
    //var expenseCategList = mutableListOf<Category>()
    private val type = TypeToken.getParameterized(List::class.java, Category::class.java).type
    private val filename = "expCatsRepo.json"

    private val _data = MutableLiveData(defaultExpList)



    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()){
            context.openFileInput(filename).bufferedReader().use{
                expenses = gson.fromJson(it, type)
                _data.value = expenses
            }
        }else {
            expenses = defaultExpList
            _data.value = expenses
            sync()
        }
    }

    override fun getAll(): LiveData<List<Category>> = _data

    override fun save(category: Category){
        expenses = expenses.map {
            if (it.id!= category.id){
                it.copy(id=it.id+1)
            } else it.copy(id = 1)
        }
        expenses = expenses.sortedBy { it.id }
        _data.value = expenses
//        val changedCat = expenseCategList.findLast { it.name == category.name }
//        if (changedCat != null) {
//            expenseCategList[0] = changedCat
//        }
        sync()
    }

    private fun sync(){
        Log.i("LIFE", "sync")
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(expenses))
        }
    }
}