package com.javacat.easybudget.data

import com.javacat.easybudget.R
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.models.Type

class ExpenseCategData {

    private val expenseCategories = listOf(
        Category(1,"Продукты", R.drawable.expenses_shopping_cart_48, Type.EXPENSES),
        Category(2,"Кафе", R.drawable.expenses_fastfood_48, Type.EXPENSES),
        Category(3,"Шоппинг", R.drawable.expenses_shopping_bag_48, Type.EXPENSES),
        Category(4,"Транспорт", R.drawable.expenses_directions_bus_48, Type.EXPENSES),
        Category(5,"Здоровье", R.drawable.expenses_medication_48, Type.EXPENSES),
        Category(6,"Авто", R.drawable.expenses_directions_car_48, Type.EXPENSES),
        Category(7,"Техника", R.drawable.expenses_computer_48, Type.EXPENSES),
        Category(8,"Уют", R.drawable.expenses_home_48, Type.EXPENSES),
        Category(9,"Путешествия", R.drawable.expenses_airplane_ticket_48, Type.EXPENSES),
        Category(10,"Комунальные платежи", R.drawable.expenses_water_damage_48, Type.EXPENSES),
        Category(11,"Кредит", R.drawable.expenses_balance_48, Type.EXPENSES),
        Category(12,"Ипотека", R.drawable.expenses_home_work_48, Type.EXPENSES),
        Category(13,"Учёба", R.drawable.expenses_school_24, Type.EXPENSES),

    )

    fun getAll(): List<Category> = expenseCategories
}