package com.javacat.easybudget.domain.models

import java.time.LocalDate
import java.util.Calendar
import java.util.Date


data class BudgetItem(
    val id: Long,
    val name: String,
    val category: Category,
    val cost: Int,
    val date: LocalDate
) {
    override fun toString(): String {
        return name
    }
}