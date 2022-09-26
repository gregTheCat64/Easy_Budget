package com.javacat.easybudget.domain.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.javacat.easybudget.data.BudgetItemRepositoryFileImpl
import com.javacat.easybudget.data.BudgetRepositoryImpl
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Type

class BudgetItemViewModel(application: Application): AndroidViewModel(application) {
    private val repository = BudgetItemRepositoryFileImpl(context = application)

    val data = repository.getAll()
    fun removeById(id: Long) {
        repository.removeById(id)
        Log.i("MyLog", "viewModelRemoving")

    }
    fun save(budgetItem: BudgetItem) {
        repository.save(budgetItem)
    }
    fun edit(budgetItem: BudgetItem){
        repository.save(budgetItem)
    }

    val expenseItems = repository.getExpenses()
    val incomeItems = repository.getIncomes()
}