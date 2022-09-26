package com.javacat.easybudget.domain.models


data class BudgetItem(
    val id: Long,
    val name: String,
    val category: Category,
    val cost: Int
) {
}