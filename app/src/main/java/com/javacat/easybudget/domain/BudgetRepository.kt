package com.javacat.easybudget.domain

import androidx.lifecycle.LiveData

interface BudgetRepository {
    fun getStartBudget(): LiveData<Int>
    fun saveStartBudget(budgetValue:Int)
}