package com.javacat.easybudget.domain.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.javacat.easybudget.data.BalanceRepositoryImpl
import com.javacat.easybudget.data.BudgetRepositoryFileImpl
import com.javacat.easybudget.domain.BudgetRepository
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Type

class BudgetViewModel(application: Application): AndroidViewModel(application) {
    private val repository: BudgetRepository = BudgetRepositoryFileImpl(context = application)
    private val startBalanceRepository = BalanceRepositoryImpl(context = application)

    val data = repository.getAll()
    val expensesData = repository.getExpenses()
    val incomesData = repository.getIncomes()
    val currentBalance = MutableLiveData<Int>()

    fun save(budgetItem:BudgetItem){
        repository.save(budgetItem)
    }

    fun removeById(id:Long) = repository.removeById(id)

    fun saveMonth(month: Int) {
        repository.saveCurrentMonth(month)
        Log.i("MyLog", "save $month")
    }

    fun getCurrentBalance():LiveData<Int> {
        val data = repository.getAll().value as List
        val expenseList = data.filter { it.category.type == Type.EXPENSES }
        val incomeList = data.filter { it.category.type == Type.INCOMES }
        Log.i("MyTag", "$expenseList in viewModel")
        var expenses = 0
        var incomes = 0

        val startBudgetData = startBalanceRepository.getStartBudget()
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
       currentBalance.value = startBudget-expenses+incomes
        return currentBalance
    }

    fun saveStartBalance(startBalance: Int){
        startBalanceRepository.saveStartBudget(startBalance)
    }
}

