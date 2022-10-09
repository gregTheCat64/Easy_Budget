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
        val nowDate = LocalDate.now()
        var currentDate = nowDate

        //val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
        binding.currentMonth.text = "${currentDate.month}"
        budgetViewModel.setDay(currentDate)
        Log.i("TIME", "in Charts Activity: $currentDate")

        //кнопки взад вперед:
        binding.previousBtn.setOnClickListener {
          currentDate = currentDate.minusMonths(1)
            budgetViewModel.setDay(currentDate)
            binding.currentMonth.text = "${currentDate.month}"

        }

        binding.nextBtn.setOnClickListener {
            currentDate = currentDate.plusMonths(1)
            budgetViewModel.setDay(currentDate)
            binding.currentMonth.text = "${currentDate.month}"
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