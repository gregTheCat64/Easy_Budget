package com.javacat.easybudget.domain.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.javacat.easybudget.data.BudgetRepositoryFileImpl
import com.javacat.easybudget.data.RegularRepositoryImpl
import com.javacat.easybudget.data.StartDateSavingsRepository
import com.javacat.easybudget.domain.BudgetRepository
import com.javacat.easybudget.domain.RegularRepository
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Type
import java.time.LocalDate
import kotlin.math.roundToInt

class RegularSpendingsViewModel(application: Application): AndroidViewModel(application) {

    private val repository: RegularRepository = RegularRepositoryImpl(context = application)
    private val startDateSavingsRepository = StartDateSavingsRepository(context = application)
    private val curRepository: BudgetRepository = BudgetRepositoryFileImpl(context = application)

    private val sumRecommended = MutableLiveData<Int>()

    val regData = repository.getAll()
    val curData = curRepository.getAll()
    var currentDate = LocalDate.now()
    var regularExpenses = 0
    var regularIncomes = 0
    private val regExpData = MutableLiveData<List<BudgetItem>>()
    private val regIncData = MutableLiveData<List<BudgetItem>>()
    lateinit var regularExpensesList: List<BudgetItem>
    lateinit var regularIncomesList: List<BudgetItem>


    fun save(budgetItem: BudgetItem){
        repository.save(budgetItem)
    }

    fun updateLists(){
        val regList = regData.value as List
        regularExpensesList = regList.filter { it.category.type == Type.EXPENSES }
        regExpData.value = regularExpensesList
        regularIncomesList = regList.filter { it.category.type == Type.INCOMES }
        regIncData.value = regularIncomesList
    }

    fun getRegExpenses(): LiveData<List<BudgetItem>> {
        updateLists()
        return regExpData
    }
    fun getIncExpenses(): LiveData<List<BudgetItem>> {
        updateLists()
        return regIncData
    }
    fun removeById(id:Long){
        repository.removeById(id)
    }

    fun save(startDate: LocalDate){
        startDateSavingsRepository.saveStartDate(startDate)
    }


    fun getSumRecommended(): LiveData<Int> {
        val dataList = curData.value as List
        updateLists()
        regularExpenses = 0
        regularIncomes = 0
        val currentExpList = dataList.filter { it.category.type == Type.EXPENSES }
        Log.i("MY_VM", "currentExplist: $currentExpList")

        for (i in regularExpensesList){
            regularExpenses += i.cost
        }
        for (i in regularIncomesList){
            regularIncomes += i.cost
        }

        var startDate: LocalDate? = startDateSavingsRepository.getStartDate()


        if (regularIncomes != 0) {
            var filteredByDayExpList: List<BudgetItem>?
            var expensesSum = 0
            var daylySum = ((regularIncomes.toDouble() - regularExpenses) / 30).roundToInt()
            Log.i("MY_VM", "expensesFilter stDate: $startDate")
            var daysCount = 0

            if (startDate != null && currentDate !=null){
                while (startDate?.isBefore(currentDate.plusDays(1)) == true) {

                    filteredByDayExpList =
                        currentExpList.filter {
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
                Log.i("MY_VM", "getSumEXPsum :${expensesSum}")
                Log.i("MY_VM", "getSumDayleSUM :${daylySum}")
                Log.i("MY_VM", "getSumDays :${daysCount}")
            }
        }
        Log.i("MY_VM", "getSumRecom :${sumRecommended.value}")

        return sumRecommended
    }
}