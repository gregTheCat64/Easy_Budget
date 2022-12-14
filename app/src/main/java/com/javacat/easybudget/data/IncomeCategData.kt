package com.javacat.easybudget.data

import com.javacat.easybudget.R
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.models.Type

class IncomeCategData {

    private val incomeCategories = listOf<Category>(
        Category(1,"Работа", R.drawable.incomes_badge_48, Type.INCOMES),
        Category(2,"Дивиденды", R.drawable.incomes_money_48, Type.INCOMES),
        Category(3,"Аренда", R.drawable.incomes_bed_48, Type.INCOMES),
        Category(4,"Подработка", R.drawable.incomes_camera_48, Type.INCOMES),
    )


    fun getAll() = incomeCategories
}