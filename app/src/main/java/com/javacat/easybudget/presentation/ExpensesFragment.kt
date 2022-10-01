package com.javacat.easybudget.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.javacat.easybudget.R
import com.javacat.easybudget.databinding.FragmentExpensesBinding
import com.javacat.easybudget.domain.adapters.MainAdapter
import com.javacat.easybudget.domain.adapters.OnListener
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.presentation.Charts.Companion.updatePieChart


class ExpensesFragment : Fragment() {
    private val budgetViewModel: BudgetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExpensesBinding.inflate(inflater, container, false)
        binding.recViewExpenses.layoutManager = LinearLayoutManager(context)

        val mainAdapter = MainAdapter(
            object : OnListener {
                override fun onRemove(budgetItem: BudgetItem) {
                    budgetViewModel.removeById(budgetItem.id)
                    Log.i("MyLog", "fragmentRemoving")
                    binding.recViewExpenses.smoothScrollToPosition(0)
                    Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                    budgetViewModel.getCurrentBalance()
                }
            }
        )
        binding.recViewExpenses.adapter = mainAdapter
        val pieChart = binding.pieChartExpenses
        budgetViewModel.expensesData.observe(viewLifecycleOwner) { expenses ->
            mainAdapter.submitList(expenses)
            updatePieChart(expenses, pieChart, requireContext())
        }

        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = ExpensesFragment()
    }

}