package com.javacat.easybudget.domain

import androidx.lifecycle.LiveData
import com.javacat.easybudget.domain.models.Category

interface ExpCatsRepository {
    fun getAll():LiveData<List<Category>>
    fun save(category: Category)
}