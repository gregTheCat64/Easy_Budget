package com.javacat.easybudget.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.javacat.easybudget.databinding.FragmentIncomesChartBinding
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel


class IncomesChartFragment : Fragment() {
    private val budgetViewModel: BudgetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIncomesChartBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        val pieChart = binding.pieChartIncomes
        budgetViewModel.incomesDataByMonth.observe(viewLifecycleOwner) { incomes->
            Charts.updatePieChart(incomes, pieChart, requireContext())
        }
        return binding.root
    }

    companion object {


        @JvmStatic
        fun newInstance() =
            IncomesChartFragment()
    }
}