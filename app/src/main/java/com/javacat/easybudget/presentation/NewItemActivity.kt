package com.javacat.easybudget.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.javacat.easybudget.R
import com.javacat.easybudget.data.ExpenseCategData
import com.javacat.easybudget.databinding.ActivityMainBinding
import com.javacat.easybudget.databinding.ActivityNewItemBinding
import com.javacat.easybudget.domain.adapters.CategoryAdapter
import com.javacat.easybudget.domain.adapters.MainVpAdapter
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.models.Type
import com.javacat.easybudget.domain.viewmodels.BudgetItemViewModel
import com.javacat.easybudget.domain.viewmodels.CategoryViewModel
import kotlin.math.E

class NewItemActivity : AppCompatActivity() {
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val budgetItemViewModel: BudgetItemViewModel by viewModels()
    private val fragList = listOf(
        NewExpenseFragment.newInstance(),
        NewIncomeFragment.newInstance()
    )
    private val fragListTitles = listOf(
        "Расходы",
        "Доходы"
    )
    var category:Category = Category("",0, Type.EXPENSES)

    private lateinit var binding: ActivityNewItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        categoryViewModel.getCategory().observe(this) {
            binding.choosenCategory.setText(it.name)
            category = it
        }

        val adapter = MainVpAdapter(this, fragList as List<Fragment>)
        binding.newItemViewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.newItemViewPager) { tab, pos ->
            tab.text = fragListTitles[pos]
        }.attach()

        binding.addNewItemBtn.setOnClickListener {
            save()
        }
        binding.saveBtn.setOnClickListener {
            save()
        }
        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
    private fun save(){
        var categoryOfItem: Category? = null
        var priceOfItem = 0
        if (binding.priceOfBudgetItem.text.isNotBlank()){
            priceOfItem = binding.priceOfBudgetItem.text.toString().toInt()
        } else {
            Toast.makeText(applicationContext, "Поле цены пустое", Toast.LENGTH_SHORT).show()
            return
        }

        val nameOfItem = if(binding.nameOfBudgetItem.text.isBlank()){
            category.name
        } else {
            binding.nameOfBudgetItem.text.toString()
        }

        if (category.name.isNotBlank()){
            categoryOfItem = category
        } else{
            Toast.makeText(applicationContext, "Выберите категорию", Toast.LENGTH_SHORT).show()
            return
        }
        budgetItemViewModel.save(BudgetItem(
            0,
            nameOfItem,
            categoryOfItem,
            priceOfItem
        ))
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}