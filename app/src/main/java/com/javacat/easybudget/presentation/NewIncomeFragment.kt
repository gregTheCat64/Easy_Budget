package com.javacat.easybudget.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.javacat.easybudget.R
import com.javacat.easybudget.data.IncomeCategData
import com.javacat.easybudget.databinding.FragmentNewIncomeBinding
import com.javacat.easybudget.domain.adapters.CategoryAdapter
import com.javacat.easybudget.domain.models.Category
import com.javacat.easybudget.domain.viewmodels.CategoryViewModel


class NewIncomeFragment : Fragment(), CategoryAdapter.Listener {
    private val categoryViewModel: CategoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewIncomeBinding.inflate(inflater, container, false)
        binding.incomesRecView.layoutManager = GridLayoutManager(context, 2)

        val incomeCategList = IncomeCategData().getAll()
        val adapter = CategoryAdapter(incomeCategList, this)
        binding.incomesRecView.adapter = adapter

        return binding.root
    }

    override fun onClick(category: Category) {
        categoryViewModel.save(category)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            NewIncomeFragment()
    }
}

