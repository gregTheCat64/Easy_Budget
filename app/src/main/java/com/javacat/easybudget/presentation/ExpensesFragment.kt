package com.javacat.easybudget.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.javacat.easybudget.databinding.FragmentExpensesBinding
import com.javacat.easybudget.domain.adapters.MainAdapter
import com.javacat.easybudget.domain.adapters.OnListener
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.viewmodels.BudgetItemViewModel
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel


class ExpensesFragment : Fragment() {
    private val budgetItemViewModel:BudgetItemViewModel by activityViewModels()
    private val budgetViewModel:BudgetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentExpensesBinding.inflate(inflater, container,false)
        binding.recViewExpenses.layoutManager  = LinearLayoutManager(context)
        val mainAdapter = MainAdapter(
            object : OnListener {
                override fun onRemove(budgetItem: BudgetItem) {
                    budgetItemViewModel.removeById(budgetItem.id)
                    Log.i("MyLog", "fragmentRemoving")
                    binding.recViewExpenses.smoothScrollToPosition(0)
                    Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                    budgetViewModel.calculateCurrentBudget() //??????
                }
            }
        )
        binding.recViewExpenses.adapter = mainAdapter

        budgetItemViewModel.expenseItems.observe(viewLifecycleOwner) {expenses->
            mainAdapter.submitList(expenses)
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExpensesFragment()
    }

}