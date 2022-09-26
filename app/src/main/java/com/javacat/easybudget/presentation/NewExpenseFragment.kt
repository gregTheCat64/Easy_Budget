package com.javacat.easybudget.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.javacat.easybudget.R
import com.javacat.easybudget.data.ExpenseCategData
import com.javacat.easybudget.databinding.FragmentNewExpenseBinding
import com.javacat.easybudget.domain.adapters.CategoryAdapter
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.domain.viewmodels.CategoryViewModel

class NewExpenseFragment : Fragment(), CategoryAdapter.Listener {
    private val categViewModel: CategoryViewModel by activityViewModels()
    private val budgetViewModel:BudgetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewExpenseBinding.inflate(inflater,container,false)
        binding.expensesRecView.layoutManager = GridLayoutManager(context, 2)

        val expenseCategList = ExpenseCategData().getAll()
        val adapter = CategoryAdapter(expenseCategList, this)
        binding.expensesRecView.adapter = adapter

        return binding.root
    }

    override fun onClick(category: Category) {
        categViewModel.save(category)
        budgetViewModel.calculateCurrentBudget()
        Log.d("LALA", category.name)
    //Toast.makeText(context, "clicked ${category.name}", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NewExpenseFragment()
    }


}