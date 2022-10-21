package com.javacat.easybudget.presentation

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.javacat.easybudget.R
import com.javacat.easybudget.databinding.ActivityMainBinding
import com.javacat.easybudget.databinding.StartBudgetDialogBinding
import com.javacat.easybudget.domain.adapters.ItemAdapter
import com.javacat.easybudget.domain.adapters.OnItemListener
import com.javacat.easybudget.domain.models.BudgetDataModel
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.utils.AndroidUtils
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private val budgetViewModel: BudgetViewModel by viewModels()

    private val PREFS_FILE = "PrefFile"
    private val PREF_IS_NEW = "isNewUser"

    private lateinit var binding: ActivityMainBinding
    private val todayDate = Calendar.getInstance()
    private var currentDate = Calendar.getInstance()
    private lateinit var recViewAdapter: ItemAdapter
    private lateinit var itemsMap: Map<Int, List<BudgetItem>>
    private lateinit var mList: ArrayList<BudgetDataModel>

    private var startBudgetValue = 0
    private var currentBudgetValue: Int? = startBudgetValue
    private var sumToRecommend = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateUi()
        //получаем данные
        val settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE)

        //получаем текущий месяц
        //Календарь
        budgetViewModel.setDay(currentDate)
        Log.i("TIME", "date in main$currentDate")

        //проверка работы подсчета рек.суммы:
        val startDate = Calendar.getInstance()
        startDate.set(2022,9,19)
        budgetViewModel.saveStartDate(startDate)


        //получаем тек. баланс
        budgetViewModel.getCurrentBalance().observe(this) {
            currentBudgetValue = it
            Log.i("LIFE", "getCURbalanceOBSERVER")
            updateUi()
        }


        budgetViewModel.getSumRecommended().observe(this) {
            sumToRecommend = it
            Log.i("LIFE", "getSUMRecOBSERVER")
            updateUi()
        }


        //диалог при первом запуске
        val isNewUser = settings.getBoolean(PREF_IS_NEW, true)
        if (isNewUser) (
                showDialog(settings)
                )

        //редактируем баланс:
        binding.startBalance.setOnClickListener {
            showDialog(settings)
        }


        //кнопки взад вперед:
        binding.previousBtn.setOnClickListener {
            currentDate.add(Calendar.MONTH, -1)
            budgetViewModel.setDay(currentDate)
            updateUi()
            //updateList()
        }

        binding.nextBtn.setOnClickListener {
            currentDate.add(Calendar.MONTH, 1)
            budgetViewModel.setDay(currentDate)
            updateUi()
        }



        binding.addBtn.setOnClickListener {
            startActivity(Intent(this, NewItemActivity::class.java))
//            supportFragmentManager
//                .beginTransaction().replace(R.id.mainViewPager,NewExpenseFragment.newInstance()).commit()
        }

        binding.toChartsBtn.setOnClickListener {
            startActivity(Intent(this, ChartActivity::class.java))
        }

        binding.infoCardView.setOnClickListener {
            startActivity(Intent(this, RegularSpendingsActivity::class.java))
        }

        //recView
        mList = ArrayList()
        budgetViewModel.getDayListByMonth().observe(this) { dayList ->
            updateList()
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

//    private showEconomyDialog(){
//
//    }

    private fun updateUi() {
        Log.i("LIFE", "updateUi")

        binding.startBalance.text = "$currentBudgetValue руб."
        binding.RecSumTextView.text = "$sumToRecommend руб."
        //val dateFormatter = DateFormat.getDateInstance(Calendar.MONTH)
        val dateFormatter = SimpleDateFormat("MMM YY")
        binding.currentDay.text = dateFormatter.format(currentDate.time)

        if (currentDate == todayDate) {
            binding.apply {
                nextBtn.isClickable = false
                nextBtn.alpha = 0.2F
            }
        } else {
            binding.apply {
                nextBtn.isClickable = true
                nextBtn.alpha = 1F
            }
        }
        Log.i("MYLOG", "budgetvalue: $sumToRecommend")

        if (sumToRecommend == 0) {
            binding.cardViewTextView.text = "Нажмите на карточку, чтобы добавить регулярные расходы"
            binding.RecSumTextView.visibility = View.INVISIBLE
        } else {
            binding.cardViewTextView.text = "Лимит расходов на сегодня:"
            binding.RecSumTextView.visibility = View.VISIBLE
        }
    }

    private fun updateList() {
        val dayList = budgetViewModel.getDayListByMonth().value
        mList.clear()
        itemsMap = emptyMap()
        if (dayList != null) {
            itemsMap = dayList.sortedByDescending { it.date }
                .groupBy { it.date.get(Calendar.DAY_OF_MONTH)}
        }

        for (items in itemsMap) {
            mList.add(BudgetDataModel(items.value, items.key))
        }
        val itemAdapter = ItemAdapter(mList, this, object: OnItemListener{
            override fun onRemove(budgetItem: BudgetItem) {
                budgetViewModel.removeById(budgetItem.id)
                budgetViewModel.getCurrentBalance()
                budgetViewModel.getSumRecommended()
            }

        })
        binding.recViewExpenses.adapter = itemAdapter
        println("mlist: $mList")
        for (i in mList){
            println(i.toString())
        }
    }
}