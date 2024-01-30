package com.maxim.diaryforstudents.performance.analytics.presentation

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxim.diaryforstudents.R

interface AnalyticsUi {
    fun showData(lineChart: LineChart)

    class Base(
        private val data: List<Pair<Float, Float>>,
        private val labels: List<String>
    ) : AnalyticsUi {

        override fun showData(lineChart: LineChart) {
            lineChart.apply {
                setDrawBorders(false)
                isDoubleTapToZoomEnabled = false
                setScaleEnabled(false)
                setTouchEnabled(false)
                isAutoScaleMinMaxEnabled = true
                legend.isEnabled = false
                description.isEnabled = false
            }

            lineChart.axisLeft.apply {
                axisLineWidth = 4f
                axisLineColor = ContextCompat.getColor(lineChart.context, R.color.back_blue)
                labelCount = 4
                setDrawGridLines(false)
                textSize = 14f
                textColor = ContextCompat.getColor(lineChart.context, R.color.green)
                typeface = Typeface.DEFAULT_BOLD
            }

            lineChart.axisRight.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
                setDrawAxisLine(false)
            }

            lineChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                axisLineColor = ContextCompat.getColor(lineChart.context, R.color.back_blue)
                axisLineWidth = 4f
                setDrawGridLines(false)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt()]
                    }
                }
                textColor = ContextCompat.getColor(lineChart.context, R.color.dark_gray)
                typeface = Typeface.DEFAULT_BOLD
            }

            val entries = arrayListOf<Entry>()
            data.forEach {
                entries.add(Entry(it.first, it.second))
            }
            val dataSet = LineDataSet(entries, "Label").apply {
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
}