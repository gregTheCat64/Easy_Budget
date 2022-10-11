package com.javacat.easybudget.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.javacat.easybudget.data.ExpenseCategData
import com.javacat.easybudget.data.IncomeCategData
import com.javacat.easybudget.databinding.FragmentRegularExpensesBinding
import com.javacat.easybudget.databinding.FragmentRegularIncomesBinding
import com.javacat.easybudget.domain.adapters.MainAdapter
import com.javacat.easybudget.domain.adapters.OnListener
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.domain.viewmodels.RegularSpendingsViewModel
import java.time.LocalDate
import java.util.*


class RegularIncomesFragment : Fragment() {

    lateinit var binding: FragmentRegularIncomesBinding
    private val regularSpendingsViewModel: RegularSpendingsViewModel by activityViewModels()
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
                    regularSpendingsViewModel.removeById(budgetItem.id)
                    binding.regIncRecView.smoothScrollToPosition(0)
                    Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                    budgetViewModel.getCurrentBalance()
                    regularSpendingsViewModel.getSumRecommended()
                    regularSpendingsViewModel.updateLists()
                }
            }
        )
        binding.regIncRecView.adapter = mainAdapter
        regularSpendingsViewModel.getIncExpenses().observe(viewLifecycleOwner) {
            mainAdapter.submitList(it)
        }

        var choosenDate = LocalDate.now()


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
                regularSpendingsViewModel.save(
                    BudgetItem(
                        0,
                        name = category.name,
                        category = category,
                        date = choosenDate,
                        cost = priceOfItem
                    )
                )

                binding.priceOfBudgetItem.setText("")
                //regularSpendingsViewModel.saveStartDate(choosenDate)
                regularSpendingsViewModel.getSumRecommended()
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