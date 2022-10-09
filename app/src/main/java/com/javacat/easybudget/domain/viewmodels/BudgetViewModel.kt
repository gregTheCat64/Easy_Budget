package com.javacat.easybudget.domain.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.javacat.easybudget.data.BalanceRepositoryImpl
import com.javacat.easybudget.data.BudgetEconomyRepository
import com.javacat.easybudget.data.BudgetRepositoryFileImpl
import com.javacat.easybudget.domain.BudgetRepository
import com.javacat.easybudget.domain.models.BudgetEconomy
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Type
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.roundToInt

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BudgetRepository = BudgetRepositoryFileImpl(context = application)
    private val startBalanceRepository = BalanceRepositoryImpl(context = application)
    private val budgetEconomyRepository: BudgetEconomyRepository =
        BudgetEconomyRepository(context = application)


    val data = repository.getAll()
    val expensesDataByMonth = repository.getExpensesByMonth()
    val incomesDataByMonth = repository.getIncomesByMonth()
    val expensesDataByDay = repository.getExpensesByDay()
    val incomesDataByDay = repository.getIncomesByDay()
    val currentBalance = MutableLiveData<Int>()
    val sumRecommended = MutableLiveData<Int>()

    //budgetEconomy:
    private val budgetEconomy = budgetEconomyRepository.getBudgetEconomyObject()

    var regularExpenses = 0
    var regularIncomes = 0

    var currentDate = LocalDate.now()
    var tempDate = LocalDate.now()


    fun save(budgetItem: BudgetItem) {
        repository.save(budgetItem)
    }

    fun removeById(id: Long) = repository.removeById(id)

    fun setDay(date: LocalDate) {
        repository.setCurrentDay(date)
    }

    fun saveStartBalance(startBalance: Int) {
        startBalanceRepository.saveStartBudget(startBalance)
    }

    fun saveBudgetEconomy(budgetEconomy: BudgetEconomy) {
        budgetEconomyRepository.save(budgetEconomy)
    }


    fun getCurrentBalance(): LiveData<Int> {
        val dataList = data.value as List
        Log.i("LIFE", "getCurBalance: ${dataList}")
        val expenseList = dataList.filter { it.category.type == Type.EXPENSES }
        val incomeList = dataList.filter { it.category.type == Type.INCOMES }
        var expenses = 0
        var incomes = 0

        val startBudgetData = startBalanceRepository.getStartBudget()
        val startBudget = startBudgetData.value as Int

        //считаем цены всех расходов
        if (expenseList.isNotEmpty()) {
            for (i in expenseList) {
                expenses += i.cost
            }
        }

        //считаем цены всех доходов
        if (incomeList.isNotEmpty()) {
            for (i in incomeList) {
                incomes += i.cost
            }
        }

        //получаем текущий баланс:
        currentBalance.value = startBudget - expenses + incomes
        return currentBalance
    }


    fun getSumRecommended(): LiveData<Int> {

        val dataList = data.value as List

        regularExpenses = budgetEconomy.regularExpensesSum
        regularIncomes = budgetEconomy.regularIncomesSum
        var startDate: LocalDate? = budgetEconomy.startDate
        if (regularExpenses != 0 && regularIncomes != 0) {

            var filteredByDayExpList: List<BudgetItem>?
            var expensesSum = 0
            var daylySum = ((regularIncomes.toDouble() - regularExpenses) / 30).roundToInt()
            val expenses = dataList.filter {
                it.category.type == Type.EXPENSES
            }

            var daysCount = 0

            if (startDate != null && currentDate !=null){
                while (startDate?.isBefore(currentDate.plusDays(1)) == true) {
                    Log.i("MY_VM", "expensesFilter stDate: $startDate")
                    filteredByDayExpList =
                        expenses.filter {
                            it.date.dayOfMonth == startDate?.dayOfMonth &&
                                    it.date.month == startDate?.month &&
                                    it.date.year == startDate?.year
                        }
                    Log.i("MY_VM", "expensesFilter: $filteredByDayExpList")
//
                    for (i in filteredByDayExpList.indices) {
                        expensesSum += filteredByDayExpList[i].cost
                    }
                    daysCount++
                    startDate = startDate?.plusDays(1)
                }
                daylySum *= daysCount
                sumRecommended.value = (daylySum - expensesSum)
                Log.i("LIFE", "getSumEXPsum :${expensesSum}")
                Log.i("LIFE", "getSumDayleSUM :${daylySum}")
                Log.i("LIFE", "getSumDays :${daysCount}")
            }
        }
        Log.i("LIFE", "getSumRecom :${sumRecommended.value}")

        return sumRecommended
    }
}


