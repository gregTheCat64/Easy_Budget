package com.javacat.easybudget.presentation

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.javacat.easybudget.R
import com.javacat.easybudget.databinding.ActivityNewItemBinding
import com.javacat.easybudget.domain.adapters.MainVpAdapter
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.models.Type
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.domain.viewmodels.CategoryViewModel
import java.text.DateFormat
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
    var category: Category = Category(0, "", 0, Type.EXPENSES)

    private lateinit var binding: ActivityNewItemBinding
    val now = Calendar.getInstance()
    val nowYear = now.get(Calendar.YEAR)
    val nowMonth = now.get(Calendar.MONTH)
    val nowDay = now.get(Calendar.DATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Календарь
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
//        val maxDate = Calendar.getInstance()
//        maxDate.set(Calendar.DAY_OF_MONTH, day)


        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.twoDaysBeforeRadBtn -> calendar.set(year, month, day.minus(2))
                R.id.yesterdayRadBtn -> calendar.set(year, month, day.minus(1))
                R.id.todayRadBtn -> {
                    calendar.set(year, month, day)
                }
            }
            binding.editTextDate.setText(dateFormatter.format(calendar.time))
        }


        binding.calendarBtn.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    calendar.set(mYear, mMonth, mDay)
                    binding.editTextDate.setText(dateFormatter.format(calendar.time))
                },
                year,
                month,
                day
            )
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
            save(calendar)
        }
        binding.saveBtn.setOnClickListener {
            save(calendar)
        }
        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun save(itemDate: Calendar) {
        Log.i("LIFE", "saveBtn")
        var categoryOfItem: Category? = null
        var priceOfItem = 0
        now.clear()

        if (binding.priceOfBudgetItem.text.isNotBlank()) {
            priceOfItem = binding.priceOfBudgetItem.text.toString().toInt()
        } else {
            Toast.makeText(applicationContext, "Поле цены пустое", Toast.LENGTH_SHORT).show()
            return
        }

        val nameOfItem = if (binding.nameOfBudgetItem.text.isBlank()) {
            category.name
        } else {
            binding.nameOfBudgetItem.text.toString()
        }

        if (category.name.isNotBlank()) {
            categoryOfItem = category
        } else {
            Toast.makeText(applicationContext, "Выберите категорию", Toast.LENGTH_SHORT).show()
            return
        }
        now.set(nowYear,nowMonth,nowDay+1)
        Log.i("REGCAL", "choosenDate: ${itemDate.time}")
        Log.i("REGCAL", "calendarDate: ${now.time}")

        when (binding.switch1.isChecked) {
            true -> {
                budgetViewModel.saveStartDate(now)
                budgetViewModel.saveRegItem(
                    BudgetItem(
                        0,
                        nameOfItem,
                        categoryOfItem,
                        priceOfItem,
                        date = itemDate
                    )
                )
                if (itemDate.time.before(now.time)){
                    budgetViewModel.save(
                        BudgetItem(
                            0,
                            nameOfItem,
                            categoryOfItem,
                            priceOfItem,
                            date = itemDate
                        )
                    )
                }

            }
            false -> {
                budgetViewModel.save(
                    BudgetItem(
                        0,
                        nameOfItem,
                        categoryOfItem,
                        priceOfItem,
                        date = itemDate
                    )
                )
            }
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}