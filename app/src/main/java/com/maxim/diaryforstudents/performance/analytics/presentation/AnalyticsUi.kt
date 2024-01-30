package com.maxim.diaryforstudents.performance.analytics.presentation

import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxim.diaryforstudents.R

interface AnalyticsUi {
    fun showData(lineChart: LineChart) {}
    fun showData(pieChart: PieChart) {}

    class Line(
        private val data: List<Float>,
        private val labels: List<String>
    ) : AnalyticsUi {

        override fun showData(lineChart: LineChart) {
            val entries = arrayListOf<Entry>()
            data.forEachIndexed { i, float ->
                entries.add(Entry(i.toFloat(), float))
            }

            lineChart.xAxis.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels.getOrNull(value.toInt()) ?: value.toString()
                    }
                }
            }

            lineChart.setNoDataText(lineChart.context.getString(R.string.no_data))
            lineChart.setNoDataTextColor(ContextCompat.getColor(lineChart.context, R.color.black))
            val dataSet = LineDataSet(entries, "").apply {
                color = ContextCompat.getColor(lineChart.context, R.color.light_green)
                lineWidth = 4f
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawValues(false)
                highLightColor = ContextCompat.getColor(lineChart.context, R.color.green)
            }
            dataSet.setDrawCircles(false)
            val lineData = LineData(dataSet)
            lineChart.data = lineData
        }
    }

    class Pie(
        private val fiveCount: Int,
        private val fourCount: Int,
        private val threeCount: Int,
        private val twoCount: Int,
    ) : AnalyticsUi {
        override fun showData(pieChart: PieChart) {
            val sum = fiveCount + fourCount + threeCount + twoCount
            val entries = hashMapOf(
                "5 (${(fiveCount.toFloat() / sum * 100).toInt()}%)" to fiveCount,
                "4 (${(fourCount.toFloat() / sum * 100).toInt()}%)" to fourCount,
                "3 (${(threeCount.toFloat() / sum * 100).toInt()}%)" to threeCount,
                "2 (${(twoCount.toFloat() / sum * 100).toInt()}%)" to twoCount
            ).map {
                PieEntry(it.value.toFloat(), it.key)
            }
            val colors =
                arrayListOf(R.color.yellow, R.color.light_green, R.color.green, R.color.red).map {
                    ContextCompat.getColor(pieChart.context, it)
                }.reversed()

            val dataSet = PieDataSet(entries, "").apply {
                valueTextSize = 16f
                setColors(colors)
                valueTextColor = ContextCompat.getColor(pieChart.context, R.color.black)
            }
            val pieData = PieData(dataSet).apply {
                setValueFormatter(object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}"
                    }
                })
            }
            pieChart.data = pieData
        }
    }

    object Error : AnalyticsUi {
        override fun showData(lineChart: LineChart) {
            lineChart.setNoDataText("Fail")
            lineChart.data = null
        }
    }
}