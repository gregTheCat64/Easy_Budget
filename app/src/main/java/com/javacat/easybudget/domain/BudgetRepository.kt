package com.javacat.easybudget.domain

import androidx.lifecycle.LiveData
import com.javacat.easybudget.domain.models.BudgetItem
import java.time.LocalDate
import java.util.Calendar

interface BudgetRepository {
    fun getAll():LiveData<List<BudgetItem>>
    fun removeById(id:Long)
    fun save(budgetItem: BudgetItem)
    fun getIncomesByMonth():LiveData<List<BudgetItem>>
    fun getExpensesByMonth():LiveData<List<BudgetItem>>
    fun getIncomesByDay():LiveData<List<BudgetItem>>
    fun getExpensesByDay():LiveData<List<BudgetItem>>
    fun setCurrentDay(day:LocalDate)
}