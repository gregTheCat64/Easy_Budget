package com.javacat.easybudget.domain.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.javacat.easybudget.data.BudgetItemRepositoryFileImpl
import com.javacat.easybudget.data.BudgetRepositoryImpl
import com.javacat.easybudget.domain.models.BudgetItem

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetRepository = BudgetRepositoryImpl(application)
    private val repo = BudgetItemRepositoryFileImpl(application)

    private val _currentBudgetData = MutableLiveData<Int>()
    val currentBudgetData = _currentBudgetData



    fun calculateCurrentBudget() {
        val expenseList = repo.getExpenses().value as List
        val incomeList = repo.getIncomes().value as List

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

    fun save(startBudget: Int) {
        budgetRepository.saveStartBudget(startBudget)
    }
}