package com.javacat.easybudget.data

import com.javacat.easybudget.R
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.models.Type

class ExpenseCategData {

    private val expenseCategories = listOf(
        Category("Продукты", R.drawable.expenses_shopping_cart_48, Type.EXPENSES),
        Category("Кафе", R.drawable.expenses_fastfood_48, Type.EXPENSES),
        Category("Шоппинг", R.drawable.expenses_shopping_bag_48, Type.EXPENSES),
        Category("Транспорт", R.drawable.expenses_directions_bus_48, Type.EXPENSES),
        Category("Здоровье", R.drawable.expenses_medication_48, Type.EXPENSES),
        Category("Авто", R.drawable.expenses_directions_car_48, Type.EXPENSES),
        Category("Техника", R.drawable.expenses_computer_48, Type.EXPENSES),
        Category("Уют", R.drawable.expenses_home_48, Type.EXPENSES),
        Category("Путешествия", R.drawable.expenses_airplane_ticket_48, Type.EXPENSES),
        Category("Комунальные платежи", R.drawable.expenses_water_damage_48, Type.EXPENSES),
        Category("Кредит", R.drawable.expenses_balance_48, Type.EXPENSES),
        Category("Ипотека", R.drawable.expenses_home_work_48, Type.EXPENSES),
        Category("Учёба", R.drawable.expenses_school_24, Type.EXPENSES),

    )

    fun getAll(): List<Category> = expenseCategories
}