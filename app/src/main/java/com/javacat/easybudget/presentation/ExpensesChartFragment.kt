package com.javacat.easybudget.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.javacat.easybudget.databinding.FragmentExpensesChartBinding
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel


class ExpensesChartFragment : Fragment() {
    private val budgetViewModel:BudgetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExpensesChartBinding.inflate(inflater,container,false)

        val pieChart = binding.pieChartExpenses
        budgetViewModel.expensesDataByMonth.observe(viewLifecycleOwner) { expenses ->
            Charts.updatePieChart(expenses, pieChart, requireContext())
            Log.i("PIE", "${expenses.size}")
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ExpensesChartFragment()
    }
}