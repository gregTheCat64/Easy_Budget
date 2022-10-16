package com.javacat.easybudget.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.javacat.easybudget.data.IncomeCategData
import com.javacat.easybudget.databinding.FragmentRegularIncomesBinding
import com.javacat.easybudget.domain.adapters.MainAdapter
import com.javacat.easybudget.domain.adapters.OnListener
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import java.util.*


class RegularIncomesFragment : Fragment() {

    lateinit var binding: FragmentRegularIncomesBinding
    private val budgetViewModel: BudgetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegularIncomesBinding.inflate(inflater,container,false)
        val incomesList = IncomeCategData().getAll()
        val adapter: ArrayAdapter<Category> = ArrayAdapter<Category>(requireContext(),android.R.layout.simple_spinner_item, incomesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.regIncSpinner.adapter = adapter
        binding.regIncRecView.layoutManager = LinearLayoutManager(context)

        val mainAdapter = MainAdapter(
            object : OnListener {
                override fun onRemove(budgetItem: BudgetItem) {
                    budgetViewModel.removeRegById(budgetItem.id)
                    binding.regIncRecView.smoothScrollToPosition(0)
                    Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                    budgetViewModel.getCurrentBalance()
                    budgetViewModel.getSumRecommended()
                    budgetViewModel.updateRegLists()
                }
            }
        ,requireContext())
        binding.regIncRecView.adapter = mainAdapter
        budgetViewModel.getIncExpenses().observe(viewLifecycleOwner) {
            mainAdapter.submitList(it)
        }

        var choosenDate = Calendar.getInstance()


        binding.addBtn.setOnClickListener {
            var priceOfItem = 0
            val choosenCategory = binding.regIncSpinner.selectedItem.toString()
            val category = incomesList.find { it.name == choosenCategory }

            if (binding.priceOfBudgetItem.text.isNotBlank()) {
                priceOfItem = binding.priceOfBudgetItem.text.toString().toInt()
            } else {
                Toast.makeText(context, "Поле цены пустое", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (category != null){
                budgetViewModel.saveRegItem(
                    BudgetItem(
                        0,
                        name = category.name,
                        category = category,
                        date = choosenDate,
                        cost = priceOfItem
                    )
                )

                binding.priceOfBudgetItem.setText("")
                budgetViewModel.saveStartDate(choosenDate)
                budgetViewModel.getSumRecommended()
            }else return@setOnClickListener
        }


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegularIncomesFragment()
    }
}