package com.javacat.easybudget.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.javacat.easybudget.R
import com.javacat.easybudget.databinding.FragmentIncomesBinding
import com.javacat.easybudget.domain.adapters.MainAdapter
import com.javacat.easybudget.domain.adapters.OnListener
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.viewmodels.BudgetItemViewModel


class IncomesFragment : Fragment() {
    private val budgetItemViewModel:BudgetItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentIncomesBinding.inflate(inflater,container,false)
        binding.recViewIncomes.layoutManager = LinearLayoutManager(context)
        val mainAdapter = MainAdapter(
            object : OnListener{
                override fun onRemove(budgetItem: BudgetItem) {
                    budgetItemViewModel.removeById(budgetItem.id)
                    binding.recViewIncomes.smoothScrollToPosition(0)
                    Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                }
            }
        )
        binding.recViewIncomes.adapter = mainAdapter
        budgetItemViewModel.incomeItems.observe(viewLifecycleOwner) {incomes->
            mainAdapter.submitList(incomes)
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = IncomesFragment()
    }
}