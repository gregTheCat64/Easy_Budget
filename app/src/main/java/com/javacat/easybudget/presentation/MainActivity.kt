package com.javacat.easybudget.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.javacat.easybudget.R
import com.javacat.easybudget.data.BudgetItemRepositoryFileImpl
import com.javacat.easybudget.databinding.ActivityMainBinding
import com.javacat.easybudget.databinding.StartBudgetDialogBinding
import com.javacat.easybudget.domain.adapters.MainVpAdapter
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.viewmodels.BudgetItemViewModel
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.utils.AndroidUtils

class MainActivity : AppCompatActivity() {
    private val budgetItemViewModel:BudgetItemViewModel by viewModels()
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
    private var currentBudgetValue = startBudgetValue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //получаем данные
        val settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
        budgetViewModel.calculateCurrentBudget()
        budgetViewModel.currentBudgetData.observe(this){
            Log.i("MyLog", "mainObserver")
            currentBudgetValue = it
            Toast.makeText(this, "$currentBudgetValue", Toast.LENGTH_SHORT).show()
            updateUi()
        }

        //диалог при первом запуске
        val isNewUser = settings.getBoolean(PREF_IS_NEW, true)
        if (isNewUser)(
                showDialog(settings)
        )

        binding.startBudget.setOnClickListener {
            showDialog(settings)
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
                budgetViewModel.save(startBudget)
                updateUi()
                dialog.dismiss()
            }
        }
        dialog.setOnDismissListener { AndroidUtils.hideKeyboard(dialogBinding.editStartBudget) }
        dialog.show()
    }

    private fun updateUi(){
        binding.startBudget.text = currentBudgetValue.toString()
        Log.i("MyLog", "updateUi")
    }


}