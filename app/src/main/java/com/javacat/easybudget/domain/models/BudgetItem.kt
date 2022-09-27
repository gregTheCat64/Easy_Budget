package com.javacat.easybudget.domain.models

import java.util.Date


data class BudgetItem(
    val id: Long,
    val name: String,
    val category: Category,
    val cost: Int,
    val date:Date
) {
}