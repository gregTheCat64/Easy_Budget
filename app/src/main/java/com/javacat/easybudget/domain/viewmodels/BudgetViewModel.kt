package com.javacat.easybudget.domain.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.javacat.easybudget.data.*
import com.javacat.easybudget.domain.BudgetRepository
import com.javacat.easybudget.domain.ExpCatsRepository
import com.javacat.easybudget.domain.RegularRepository
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.models.Type
import org.intellij.lang.annotations.JdkConstants.CalendarMonth
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val budgetRepository: BudgetRepository = BudgetRepositoryFileImpl(context = application)
    private val startBalanceRepository = BalanceRepositoryImpl(context = application)
    private val regularRepository: RegularRepository = RegularRepositoryImpl(context = application)
    private val startDateSavingsRepository = StartDateSavingsRepository(context = application)
    private val expCatsRepository: ExpCatsRepository = ExpCatsRepositoryImpl(context = application)

    val data = budgetRepository.getAll()
    val expensesDataByMonth = budgetRepository.getExpensesByMonth()
    val incomesDataByMonth = budgetRepository.getIncomesByMonth()
    val expensesDataByDay = budgetRepository.getExpensesByDay()
    val incomesDataByDay = budgetRepository.getIncomesByDay()

    private val currentBalance = MutableLiveData<Int>()
    private val sumRecommended = MutableLiveData<Int>()

    private val regData = regularRepository.getAll()

    private val expCatList = expCatsRepository.getAll()


    var regularExpenses = 0
    var regularIncomes = 0
    private val regExpData = MutableLiveData<List<BudgetItem>>()
    private val regIncData = MutableLiveData<List<BudgetItem>>()

    lateinit var regularExpensesList: List<BudgetItem>
    lateinit var regularIncomesList: List<BudgetItem>


    fun save(budgetItem: BudgetItem) {
        budgetRepository.save(budgetItem)
    }

    fun removeById(id: Long) = budgetRepository.removeById(id)

    fun setDay(date: Calendar) {
        budgetRepository.setCurrentDay(date)
    }

    fun saveStartBalance(startBalance: Int) {
        startBalanceRepository.saveStartBudget(startBalance)
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

    fun saveRegItem(budgetItem: BudgetItem){
        regularRepository.save(budgetItem)
    }

    fun updateRegLists(){
        val regList = regData.value as List
        regularExpensesList = regList.filter { it.category.type == Type.EXPENSES }
        regExpData.value = regularExpensesList
        regularIncomesList = regList.filter { it.category.type == Type.INCOMES }
        regIncData.value = regularIncomesList
    }

    fun getRegExpenses(): LiveData<List<BudgetItem>> {
        updateRegLists()
        return regExpData
    }
    fun getIncExpenses(): LiveData<List<BudgetItem>> {
        updateRegLists()
        return regIncData
    }
    fun removeRegById(id:Long){
        regularRepository.removeById(id)
    }

    fun saveStartDate(startDate: Calendar){
        startDateSavingsRepository.saveStartDate(startDate)
    }

    fun getExpCats():LiveData<List<Category>>{
       return expCatsRepository.getAll()
    }

    fun saveExpCat(category: Category){
        expCatsRepository.save(category)
    }


    fun getSumRecommended(): LiveData<Int> {
        Log.i("LIFE", "viewModel getSumRecommended")
        updateRegLists()
        val dataList = data.value as List
        val currentExpList = dataList.filter { it.category.type == Type.EXPENSES }

        val startDate = startDateSavingsRepository.getStartDate().value as Calendar
        val startYear = startDate.get(Calendar.YEAR)
        val startMonth = startDate.get(Calendar.MONTH)
        val startDay = startDate.get(Calendar.DAY_OF_MONTH)
        Log.i("MY_VM", "startDate :${startDate.time}")
        val currentDate: Calendar = Calendar.getInstance()
        val nowYear = currentDate.get(Calendar.YEAR)
        val nowMonth = currentDate.get(Calendar.MONTH)
        val nowDay = currentDate.get(Calendar.DATE)
        Log.i("MY_VM", "currentDate :${currentDate.time}")
        var expensesSum = 0

        regularExpenses = 0
        regularIncomes = 0

        Log.i("MY_VM", "curExpenses: $currentExpList")

        for (i in regularExpensesList){
            regularExpenses += i.cost
        }
        for (i in regularIncomesList){
            regularIncomes += i.cost
        }

        if (regularIncomes != 0) {
            var filteredByDayExpList: List<BudgetItem>?
            var daylySum = ((regularIncomes.toDouble() - regularExpenses) / 30).roundToInt()

            val date1 = Calendar.getInstance()
            date1.clear()
            date1.set(startYear,startMonth,startDay)
            val date2 = Calendar.getInstance()
            date2.clear()
            date2.set(nowYear,nowMonth,nowDay.plus(1))

            var daysCount = 0

            if (date1 != null){
               while (date1.time.before(date2.time)) {
                    Log.i("MY_VM", "date1 :${date1.time}")
                    Log.i("MY_VM", "date2 :${date2.time}")
                    filteredByDayExpList =
                        currentExpList.filter {
                            it.date.get(Calendar.DATE) == date1.get(Calendar.DATE) &&
                                    it.date.get(Calendar.MONTH) == date1.get(Calendar.MONTH) &&
                                    it.date.get(Calendar.YEAR) == date1.get(Calendar.YEAR)
                        }
                   // Log.i("MY_VM", "expensesFilter: $filteredByDayExpList")

                    for (i in filteredByDayExpList.indices) {
                        expensesSum += filteredByDayExpList[i].cost
                    }
                    daysCount++
                    date1.add(Calendar.DATE,1)
                }
                daylySum *= daysCount
                sumRecommended.value = (daylySum - expensesSum)
            }
            Log.i("MY_VM", "daysCount :$daysCount")
        }

        Log.i("MY_VM", "SumRecom :${sumRecommended.value}")


        return sumRecommended
    }

}


