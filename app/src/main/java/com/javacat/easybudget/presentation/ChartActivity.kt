package com.javacat.easybudget.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.javacat.easybudget.databinding.ActivityChartBinding
import com.javacat.easybudget.domain.adapters.MainVpAdapter
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.presentation.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ChartActivity : AppCompatActivity() {
    private val budgetViewModel: BudgetViewModel by viewModels()

    private val fragList = listOf(
        ExpensesChartFragment.newInstance(),
        IncomesChartFragment.newInstance()
    )
    private val fragListTitles = listOf(
        "Расходы",
        "Доходы"
    )

    private lateinit var binding: ActivityChartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //получаем текущий месяц
        //Календарь
        val nowDate = Calendar.getInstance()
        var currentDate = nowDate

        val month = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())
        binding.currentMonth.text = month.format(currentDate.time)
        budgetViewModel.setDay(currentDate)
        Log.i("TIME", "in Charts Activity: $currentDate")

        //кнопки взад вперед:
        binding.previousBtn.setOnClickListener {
          currentDate.add(Calendar.MONTH, 1)
            budgetViewModel.setDay(currentDate)
            binding.currentMonth.text = month.format(currentDate.time)

        }

        binding.nextBtn.setOnClickListener {
            currentDate.add(Calendar.MONTH, -1)
            budgetViewModel.setDay(currentDate)
            binding.currentMonth.text = month.format(currentDate.time)
        }

        val adapter = MainVpAdapter(this,fragList)
        binding.chartsViewPager.adapter = adapter
        //настройка вкладок
        TabLayoutMediator(binding.tabLayout, binding.chartsViewPager) { tab, pos ->
            tab.text = fragListTitles[pos]
        }.attach()

        binding.toMainBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}