package com.javacat.easybudget.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.javacat.easybudget.databinding.FragmentExpensesBinding
import com.javacat.easybudget.databinding.FragmentIncomesBinding
import com.javacat.easybudget.domain.adapters.MainAdapter
import com.javacat.easybudget.domain.adapters.OnListener
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.presentation.Charts.Companion.updatePieChart


class IncomesFragment : Fragment() {
    private val budgetViewModel:BudgetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentIncomesBinding.inflate(inflater,container,false)
        binding.recViewIncomes.layoutManager = LinearLayoutManager(context)
        var pieChart = binding.pieChartIncomes
        val mainAdapter = MainAdapter(
            object : OnListener{
                override fun onRemove(budgetItem: BudgetItem) {
                    budgetViewModel.removeById(budgetItem.id)
                    binding.recViewIncomes.smoothScrollToPosition(0)
                    Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                    budgetViewModel.getCurrentBalance()
                }
            }
        )
        binding.recViewIncomes.adapter = mainAdapter
        budgetViewModel.incomesData.observe(viewLifecycleOwner) { incomes->
            mainAdapter.submitList(incomes)
            updatePieChart(incomes, pieChart, requireContext())
        }
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = IncomesFragment()
    }
}