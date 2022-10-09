package com.javacat.easybudget.presentation

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.javacat.easybudget.databinding.ActivityNewItemBinding
import com.javacat.easybudget.domain.adapters.MainVpAdapter
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.models.Type
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.domain.viewmodels.CategoryViewModel
import java.time.LocalDate
import java.util.Calendar

class NewItemActivity : AppCompatActivity() {
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val budgetViewModel: BudgetViewModel by viewModels()
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

        //Календарь
        var calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var choosenDate: LocalDate = LocalDate.now()


        binding.calendarBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                binding.editTextDate.setText(""+ mDay +"/"+mMonth+"/"+mYear)
                calendar.set(mYear,mMonth,mDay)
                choosenDate = LocalDate.of(mYear,mMonth+1,mDay)
            }, year,month,day)
            dpd.show()
        }

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
            save(choosenDate)
        }
        binding.saveBtn.setOnClickListener {
            save(choosenDate)
        }
        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    private fun save(choosenDate:LocalDate){
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
        budgetViewModel.save(BudgetItem(
            0,
            nameOfItem,
            categoryOfItem,
            priceOfItem,
            date = choosenDate
        ))
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}