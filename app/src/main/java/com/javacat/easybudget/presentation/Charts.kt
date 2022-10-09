package com.javacat.easybudget.presentation

import android.content.Context
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.javacat.easybudget.R
import com.javacat.easybudget.domain.models.BudgetItem

class Charts {
    companion object{
        fun updatePieChart(list: List<BudgetItem>, pieChart: PieChart, context: Context) {
            var expenses = 0F

            val expenseEntry: ArrayList<PieEntry> = ArrayList()
            val categories = list.groupBy { it.category.name }
            var nameOfPie: String ="авфы"
            for (cats in categories) {
                var pic = ContextCompat.getDrawable(context, R.drawable.incomes_money_48)
                for (i in cats.value) {
                    expenses = 0f
                    expenses += i.cost
                    pic = ContextCompat.getDrawable(context, i.category.pic)
                    nameOfPie = i.category.type.name
                    Log.i("cats", "${i.cost}")
                    Log.i("cats", "cats")
                }
                pic?.setTint(ContextCompat.getColor(context, R.color.secondary_text))
                pic?.setBounds(0,0,200,200)
                //pic.alpha = 100
                expenseEntry.add(PieEntry(expenses,cats.key))
                //expenseEntry.add(PieEntry(expenses, cats.key, pic))

                Log.i("CatPic", "$pic")
            }
            val pieDataSet = PieDataSet(expenseEntry,"")

            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS, 100)
            //pieDataSet.setDrawIcons(true)
            var pieData = PieData(pieDataSet)
            pieChart.setData(pieData)
            pieChart.legend.formSize = 12f
            pieChart.setDrawEntryLabels(true)
            pieChart.setUsePercentValues(false)
            pieChart.setEntryLabelColor(R.color.accent_orange)
            pieChart.data.setValueTextSize(14f)
            pieChart.legend.textSize = 14f
            pieChart.description.isEnabled = false
            pieChart.centerText = nameOfPie
            pieChart.invalidate()
        }
    }
}