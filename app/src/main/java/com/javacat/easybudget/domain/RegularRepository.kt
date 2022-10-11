package com.javacat.easybudget.domain

import androidx.lifecycle.LiveData
import com.javacat.easybudget.domain.models.BudgetItem

interface RegularRepository {
    fun getAll():LiveData<List<BudgetItem>>
    fun removeById(id:Long)
    fun save(budgetItem: BudgetItem)
}