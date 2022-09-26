package com.javacat.easybudget.domain.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.javacat.easybudget.data.ExpenseCategData
import com.javacat.easybudget.domain.models.Category

class CategoryViewModel(application: Application): AndroidViewModel(application) {
    private val newCategory = MutableLiveData<Category>()

    fun getCategory() = newCategory

    fun save(category: Category) {
        newCategory.value = category
    }
}