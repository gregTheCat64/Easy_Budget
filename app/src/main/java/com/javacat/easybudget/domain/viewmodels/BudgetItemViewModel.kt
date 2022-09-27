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
    private val budgetRepository = BudgetRepositoryImpl(application)

    private val _currentBudgetData = MutableLiveData<Int>()
    val currentBudgetData = _currentBudgetData

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


    fun calculateCurrentBudget() {
        val data = repository.getAll().value as List
        val expenseList = data.filter { it.category.type == Type.EXPENSES }
        val incomeList = data.filter { it.category.type == Type.INCOMES }
        Log.i("MyTag", "$expenseList in viewModel")
        var expenses = 0
        var incomes = 0

        val startBudgetData = budgetRepository.getStartBudget()
        val startBudget = startBudgetData.value as Int
        //считаем цены всех расходов
        if (expenseList.isNotEmpty()) {
            for (i in expenseList) {
                expenses += i.cost
            }
        }
        Log.i("MyLog", "$expenses in ViewModel")
        //считаем цены всех доходов
        if (incomeList.isNotEmpty()) {
            for (i in incomeList) {
                incomes += i.cost
            }
        }
        Log.i("MyLog", "$incomes in ViewModel")
        //получаем текущий баланс:
        _currentBudgetData.value = startBudget-expenses+incomes
        currentBudgetData.value = _currentBudgetData.value

    }

    fun saveStartBudget(startBudget: Int) {
        budgetRepository.saveStartBudget(startBudget)
    }
}