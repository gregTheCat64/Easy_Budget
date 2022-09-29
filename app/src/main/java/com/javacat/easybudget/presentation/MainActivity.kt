package com.javacat.easybudget.presentation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.javacat.easybudget.databinding.ActivityMainBinding
import com.javacat.easybudget.databinding.StartBudgetDialogBinding
import com.javacat.easybudget.domain.adapters.MainVpAdapter
import com.javacat.easybudget.domain.models.Months
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.utils.AndroidUtils
import java.util.*

class MainActivity : AppCompatActivity() {
    private val budgetViewModel:BudgetViewModel by viewModels()

    private val fragList = listOf(
        ExpensesFragment.newInstance(),
        IncomesFragment.newInstance()
    )
    private val fragListTitles = listOf(
        "Расходы",
        "Доходы"
    )
    private val PREFS_FILE = "PrefFile"
    private val PREF_IS_NEW = "isNewUser"

    private lateinit var binding: ActivityMainBinding
    private  var startBudgetValue = 0
    private var currentBudgetValue: Int? = startBudgetValue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

                //получаем данные
        val settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
        budgetViewModel.getCurrentBalance().observe(this){
            currentBudgetValue = it
            updateUi()
        }
        //получаем текущий месяц
        //Календарь
        var calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        var currentMonth = month
        binding.currentMonth.text = Months.months[currentMonth]
        budgetViewModel.saveMonth(currentMonth)


        //диалог при первом запуске
        val isNewUser = settings.getBoolean(PREF_IS_NEW, true)
        if (isNewUser)(
                showDialog(settings)
        )

        //редактируем баланс:
        binding.startBalance.setOnClickListener {
            showDialog(settings)
        }


        //кнопки взад вперед:
        binding.previousBtn.setOnClickListener {
            currentMonth = currentMonth-1
            budgetViewModel.saveMonth(currentMonth)
            binding.currentMonth.text = Months.months[currentMonth]
            Log.i("MyLog", "${currentMonth} in Main")
        }

        binding.nextBtn.setOnClickListener {
            currentMonth = currentMonth+1
            budgetViewModel.saveMonth(currentMonth)
            binding.currentMonth.text = Months.months[currentMonth]
        }

        //адаптер
        val adapter = MainVpAdapter(this, fragList)
        binding.mainViewPager.adapter = adapter
        //настройка вкладок
        TabLayoutMediator(binding.tabLayout, binding.mainViewPager) { tab, pos ->
            tab.text = fragListTitles[pos]
        }.attach()

        binding.addBtn.setOnClickListener {
            startActivity(Intent(this, NewItemActivity::class.java))
        }
    }

    private fun showDialog(settings: SharedPreferences) {
        val dialogBinding = StartBudgetDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Ваш баланс")
            .setView(dialogBinding.root)
            .setMessage("Введите текущий баланс")
            .setPositiveButton("OK", null)
            .create()

        dialog.setOnShowListener {
            dialogBinding.editStartBudget.requestFocus()
            AndroidUtils.showKeyboard(dialogBinding.editStartBudget)
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = dialogBinding.editStartBudget.text.toString()
                if (enteredText.isBlank()) {
                    dialogBinding.editStartBudget.error = "Пустое значение"
                    return@setOnClickListener
                }
                val startBudget = enteredText.toIntOrNull()
                if (startBudget == null) {
                    dialogBinding.editStartBudget.error = "Неверное значение"
                    return@setOnClickListener
                }

                settings.edit().putBoolean(PREF_IS_NEW, false).apply()
                budgetViewModel.saveStartBalance(startBudget)
                budgetViewModel.getCurrentBalance()
                updateUi()
                dialog.dismiss()
            }
        }
        dialog.setOnDismissListener { AndroidUtils.hideKeyboard(dialogBinding.editStartBudget) }
        dialog.show()
    }

    private fun updateUi(){
        binding.startBalance.text = currentBudgetValue.toString()
        Log.i("MyLog", "updateUi")
    }


}