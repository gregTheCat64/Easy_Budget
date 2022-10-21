package com.javacat.easybudget.domain.models

import java.util.Calendar


data class BudgetItem(
    val id: Long,
    val name: String,
    val category: Category,
    val cost: Int,
    val date: Calendar
) {
    override fun toString(): String {
        return "$category, $cost, ${date.time}"
    }
}