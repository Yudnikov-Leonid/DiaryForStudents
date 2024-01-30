package com.maxim.diaryforstudents.performance.analytics.presentation

import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxim.diaryforstudents.R

interface AnalyticsUi {
    fun showData(lineChart: LineChart)

    class Base(
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
            val lineData = LineData(dataSet)
            lineChart.data = lineData
        }
    }

    object Error: AnalyticsUi {
        override fun showData(lineChart: LineChart) {
            lineChart.setNoDataText("Fail")
            lineChart.data = null
        }
    }

    object Loading : AnalyticsUi {
        override fun showData(lineChart: LineChart) {
            lineChart.setNoDataText("Loading...")
            lineChart.setNoDataTextColor(ContextCompat.getColor(lineChart.context, R.color.black))
            lineChart.data = null
        }
    }
}