package com.javacat.easybudget.domain.models

import java.time.LocalDate
import java.util.Calendar

class BudgetEconomy (
    val startDate: LocalDate?,
    val regularExpensesSum: Int,
    val regularIncomesSum: Int
        )
{
    companion object{
        @JvmStatic
        fun newInstance() = BudgetEconomy(startDate = null, regularExpensesSum = 0, regularIncomesSum = 0)
    }
}
