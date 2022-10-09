package com.javacat.easybudget.presentation

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.javacat.easybudget.databinding.ActivityMonthlySpendingsBinding
import com.javacat.easybudget.domain.models.BudgetEconomy
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.roundToInt

class MonthlySpendingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonthlySpendingsBinding
    private val budgetViewModel: BudgetViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMonthlySpendingsBinding.inflate(layoutInflater)
        var expensesMonthly: Int = 0
        var incomesMonthly: Int = 0
        var sumDailyToRecommend: Int
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)


            var choosenDate = LocalDate.now()

            binding.calendarBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, { view, mYear, mMonth, mDay ->
                calendar.set(mYear,mMonth,mDay)
                choosenDate = LocalDate.of(mYear,mMonth+1,mDay)
                Log.i("TIME", "choosenDate in Monthly:$choosenDate" )
            }, year,month,day)
            dpd.show()

        }


        binding.calcBtn.setOnClickListener {
            expensesMonthly = binding.monthlyExpensesSumEditText.text.toString().toInt()
            incomesMonthly = binding.monthlyIncomesSumEditText.text.toString().toInt()
            sumDailyToRecommend = ((incomesMonthly.toDouble() - expensesMonthly) / 30).roundToInt()
            val budgetEconomy = BudgetEconomy(choosenDate,expensesMonthly,incomesMonthly)
            budgetViewModel.saveBudgetEconomy(budgetEconomy)
            binding.sumToRecommendTextView.setText("Рекомендуемая сумма на каждый день: $sumDailyToRecommend")
        }
        binding.toMainBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}