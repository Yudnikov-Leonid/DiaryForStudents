package com.maxim.diaryforstudents.performance.analytics.presentation

import android.annotation.SuppressLint
import android.widget.TextView
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
    fun showTitle(textView: TextView)

    class LineCommon(
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
                setDrawCircles(false)
            }
            val lineData = LineData(dataSet)
            lineChart.data = lineData
        }

        override fun showTitle(textView: TextView) {
            val text = textView.resources.getString(R.string.common_chart)
            textView.text = text
        }
    }

    class LineMarks(
        private val fiveData: List<Float>,
        private val fourData: List<Float>,
        private val threeData: List<Float>,
        private val twoData: List<Float>,
        private val labels: List<String>
    ): AnalyticsUi {
        override fun showData(lineChart: LineChart) {
            lineChart.xAxis.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels.getOrNull(value.toInt()) ?: value.toString()
                    }
                }
            }

            val fiveEntries = fiveData.mapIndexed { i, value ->
                Entry(i.toFloat() ,value)
            }
            val fourEntries = fourData.mapIndexed { i, value ->
                Entry(i.toFloat() ,value)
            }
            val threeEntries = threeData.mapIndexed { i, value ->
                Entry(i.toFloat() ,value)
            }
            val twoEntries = twoData.mapIndexed { i, value ->
                Entry(i.toFloat() ,value)
            }

            lineChart.setNoDataText(lineChart.context.getString(R.string.no_data))
            lineChart.setNoDataTextColor(ContextCompat.getColor(lineChart.context, R.color.black))
            val fiveDataSet = LineDataSet(fiveEntries, "")
            val fourDataSet = LineDataSet(fourEntries, "")
            val threeDataSet = LineDataSet(threeEntries, "")
            val twoDataSer = LineDataSet(twoEntries, "")

            val colors = listOf(R.color.light_green, R.color.green, R.color.yellow, R.color.red)
            val dataSets = listOf(fiveDataSet, fourDataSet, threeDataSet, twoDataSer)
            dataSets.forEachIndexed { i, it ->
                it.color = ContextCompat.getColor(lineChart.context, colors[i])
                it.lineWidth = 4f
                it.mode = LineDataSet.Mode.CUBIC_BEZIER
                it.setDrawValues(false)
                it.highLightColor = ContextCompat.getColor(lineChart.context, R.color.green)
               it.setDrawCircles(false)
            }
            val lineData = LineData().apply {
                dataSets.forEach {
                    addDataSet(it)
                }
            }
            lineChart.data = lineData
        }

        override fun showTitle(textView: TextView) {
            val text = textView.resources.getString(R.string.marks_line)
            textView.text = text
        }
    }

    class PieMarks(
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

        override fun showTitle(textView: TextView) {
            val text = textView.resources.getString(R.string.marks_pie)
            textView.text = text
        }
    }

    object Error : AnalyticsUi {
        override fun showData(lineChart: LineChart) {
            lineChart.setNoDataText("Fail")
            lineChart.data = null
        }

        @SuppressLint("SetTextI18n")
        override fun showTitle(textView: TextView) {
            textView.text = "Error"
        }
    }
}