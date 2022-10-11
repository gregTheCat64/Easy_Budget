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
import com.javacat.easybudget.R
import com.javacat.easybudget.data.ExpenseCategData
import com.javacat.easybudget.databinding.ActivityRegularSpendingsBinding
import com.javacat.easybudget.databinding.FragmentExpensesChartBinding
import com.javacat.easybudget.databinding.FragmentRegularExpensesBinding
import com.javacat.easybudget.domain.adapters.MainAdapter
import com.javacat.easybudget.domain.adapters.OnListener
import com.javacat.easybudget.domain.models.BudgetEconomy
import com.javacat.easybudget.domain.models.BudgetItem
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.viewmodels.BudgetViewModel
import com.javacat.easybudget.domain.viewmodels.RegularSpendingsViewModel
import java.time.LocalDate
import java.util.Calendar

class RegularExpensesFragment : Fragment() {

    lateinit var binding: FragmentRegularExpensesBinding
    private val regularSpendingsViewModel: RegularSpendingsViewModel by activityViewModels()
    private val budgetViewModel: BudgetViewModel by activityViewModels()

    //val regularExpenses = listOf<String>("Кредит", "Ипотека", "Коммунальные платежи","Учеба", "Алименты", "Другое")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegularExpensesBinding.inflate(inflater, container, false)
        val expenseList = ExpenseCategData().getAll()
        val adapter: ArrayAdapter<Category> = ArrayAdapter<Category>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            expenseList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.regExpSpinner.adapter = adapter

        binding.regExpRecView.layoutManager = LinearLayoutManager(context)
        val mainAdapter = MainAdapter(
            object : OnListener{
                override fun onRemove(budgetItem: BudgetItem) {
                    regularSpendingsViewModel.removeById(budgetItem.id)
                    binding.regExpRecView.smoothScrollToPosition(0)
                    Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                    budgetViewModel.getCurrentBalance()
                    regularSpendingsViewModel.getSumRecommended()
                    regularSpendingsViewModel.updateLists()
                }
            }
        )
        binding.regExpRecView.adapter = mainAdapter
        regularSpendingsViewModel.getRegExpenses().observe(viewLifecycleOwner) {
            mainAdapter.submitList(it)
        }

        val choosenDate = LocalDate.of(2022, 10, 10)


        binding.addBtn.setOnClickListener {
            var priceOfItem = 0
            val choosenCategory = binding.regExpSpinner.selectedItem.toString()
            val category = expenseList.find { it.name == choosenCategory }

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
                regularSpendingsViewModel.regularExpenses
                binding.priceOfBudgetItem.setText("")
                regularSpendingsViewModel.save(choosenDate)
                regularSpendingsViewModel.getSumRecommended()

            }else return@setOnClickListener
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegularExpensesFragment()

    }
}