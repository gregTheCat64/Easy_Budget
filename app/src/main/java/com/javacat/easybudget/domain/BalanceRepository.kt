package com.javacat.easybudget.domain

import androidx.lifecycle.LiveData

interface BalanceRepository {
    fun getStartBudget(): LiveData<Int>
    fun saveStartBudget(budgetValue:Int)
}