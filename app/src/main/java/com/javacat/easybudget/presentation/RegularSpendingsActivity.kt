package com.javacat.easybudget.presentation

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.javacat.easybudget.databinding.ActivityRegularSpendingsBinding
import com.javacat.easybudget.domain.adapters.RegularVpAdapter
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.domain.viewmodels.RegularSpendingsViewModel
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.roundToInt

class RegularSpendingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegularSpendingsBinding
    private val budgetViewModel: BudgetViewModel by viewModels()
    private val regularSpendingsViewModel:RegularSpendingsViewModel by viewModels()

    private val fragList = listOf(
        RegularExpensesFragment.newInstance(),
        RegularIncomesFragment.newInstance()
    )
    private val fragListTitles = listOf(
        "Расходы",
        "Доходы"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegularSpendingsBinding.inflate(layoutInflater)

        var sumDailyToRecommend = 0

        val adapter = RegularVpAdapter(this, fragList as List<Fragment>)
        binding.regularViewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.regularViewPager) { tab, pos ->
            tab.text = fragListTitles[pos]
        }.attach()

        regularSpendingsViewModel.getSumRecommended().observe(this,{
            sumDailyToRecommend = it
            updateUi()
        }
        )

        binding.toMainBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
    fun updateUi(){
        val regularExpenses = regularSpendingsViewModel.regularExpenses
        val regularIncomes = regularSpendingsViewModel.regularIncomes
        val sumDailyToRecommend = ((regularIncomes.toDouble()-regularExpenses)/30).roundToInt()
        binding.sumExpTextView.text = "Расходы в мес.: $regularExpenses"
        binding.sumIncTextView.text = "Доходы за мес.: $regularIncomes"
        binding.sumRecToSpend.text = "Лимит на день: $sumDailyToRecommend"
    }
}