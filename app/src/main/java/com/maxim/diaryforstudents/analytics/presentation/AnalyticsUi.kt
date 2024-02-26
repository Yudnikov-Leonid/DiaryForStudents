package com.maxim.diaryforstudents.analytics.presentation

import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.ColorManager
import java.io.Serializable

interface AnalyticsUi : Serializable {
    fun same(item: AnalyticsUi): Boolean
    fun showData(lineChart: LineChart, colorManager: ColorManager) {}
    fun showData(pieChart: PieChart, colorManager: ColorManager) {}
    fun showTitle(textView: TextView)
    fun showQuarter(spinner: Spinner) {}
    fun showInterval(spinner: Spinner) {}

    fun isLine(): Boolean

    data class Title(private val lessonName: String) : AnalyticsUi {
        override fun same(item: AnalyticsUi) = item is Title

        override fun showTitle(textView: TextView) {
            if (lessonName.isEmpty()) {
                textView.text = textView.context.getString(R.string.analytics)
            } else
                textView.text = lessonName
        }

        override fun isLine() = false
    }

    data class LineCommon(
        private val data: List<Float>,
        private val labels: List<String>,
        private val quarter: Int,
        private val interval: Int
    ) : AnalyticsUi {

        override fun showQuarter(spinner: Spinner) {
            spinner.setSelection(quarter - 1)
        }

        override fun showInterval(spinner: Spinner) {
            spinner.setSelection(
                when (interval) {
                    1 -> 0
                    2 -> 1
                    3 -> 2
                    7 -> 3
                    else -> 4
                }
            )
        }

        override fun isLine() = true

        override fun same(item: AnalyticsUi) = item is LineCommon

        override fun showData(lineChart: LineChart, colorManager: ColorManager) {
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

            lineChart.legend.isEnabled = false
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

    data class LineMarks(
        private val fiveData: List<Float>,
        private val fourData: List<Float>,
        private val threeData: List<Float>,
        private val twoData: List<Float>,
        private val labels: List<String>
    ) : AnalyticsUi {
        override fun showData(lineChart: LineChart, colorManager: ColorManager) {
            lineChart.xAxis.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels.getOrNull(value.toInt()) ?: value.toString()
                    }
                }
            }
            lineChart.legend.isEnabled = true

            val fiveEntries = fiveData.mapIndexed { i, value ->
                Entry(i.toFloat(), value)
            }
            val fourEntries = fourData.mapIndexed { i, value ->
                Entry(i.toFloat(), value)
            }
            val threeEntries = threeData.mapIndexed { i, value ->
                Entry(i.toFloat(), value)
            }
            val twoEntries = twoData.mapIndexed { i, value ->
                Entry(i.toFloat(), value)
            }

            lineChart.setNoDataText(lineChart.context.getString(R.string.no_data))
            lineChart.setNoDataTextColor(ContextCompat.getColor(lineChart.context, R.color.black))
            val fiveDataSet = LineDataSet(fiveEntries, "5")
            val fourDataSet = LineDataSet(fourEntries, "4")
            val threeDataSet = LineDataSet(threeEntries, "3")
            val twoDataSer = LineDataSet(twoEntries, "2")

            val colors = listOf(
                colorManager.getColor(
                    "5",
                    ContextCompat.getColor(lineChart.context, R.color.light_green)
                ),
                colorManager.getColor(
                    "4",
                    ContextCompat.getColor(lineChart.context, R.color.green)
                ), colorManager.getColor(
                    "3",
                    ContextCompat.getColor(lineChart.context, R.color.yellow)
                ), colorManager.getColor(
                    "2",
                    ContextCompat.getColor(lineChart.context, R.color.red)
                ),
                colorManager.getColor(
                    "1",
                    ContextCompat.getColor(lineChart.context, R.color.red)
                )
            )
            val dataSets = listOf(fiveDataSet, fourDataSet, threeDataSet, twoDataSer)
            dataSets.forEachIndexed { i, it ->
                it.color = colors[i]
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

        override fun same(item: AnalyticsUi) = item is LineMarks

        override fun showTitle(textView: TextView) {
            val text = textView.resources.getString(R.string.marks_line)
            textView.text = text
        }

        override fun isLine() = true
    }

    data class PieMarks(
        private val fiveCount: Int,
        private val fourCount: Int,
        private val threeCount: Int,
        private val twoCount: Int,
    ) : AnalyticsUi {
        override fun same(item: AnalyticsUi) = item is PieMarks

        override fun showData(pieChart: PieChart, colorManager: ColorManager) {
            val sum = fiveCount + fourCount + threeCount + twoCount
            val entries = hashMapOf(
                "5" to fiveCount,
                "4" to fourCount,
                "3" to threeCount,
                "2" to twoCount
            ).mapNotNull {
                if (it.value == 0) null
                else
                    PieEntry(it.value.toFloat(), it.key)
            }
            val colors = arrayListOf<Int>()
            val legend = mutableListOf<String>()
            if (fiveCount > 0) {
                colors.add(
                    colorManager.getColor(
                        "5",
                        ContextCompat.getColor(pieChart.context, R.color.light_green)
                    )
                )
                legend.add("5 (${(fiveCount.toFloat() / sum * 100).toInt()}%)")
            }
            if (fourCount > 0) {
                colors.add(
                    colorManager.getColor(
                        "4",
                        ContextCompat.getColor(pieChart.context, R.color.green)
                    )
                )
                legend.add("4 (${(fourCount.toFloat() / sum * 100).toInt()}%)")
            }
            if (threeCount > 0) {
                colors.add(
                    colorManager.getColor(
                        "3",
                        ContextCompat.getColor(pieChart.context, R.color.yellow)
                    )
                )
                legend.add("3 (${(threeCount.toFloat() / sum * 100).toInt()}%)")
            }
            if (twoCount > 0) {
                colors.add(
                    colorManager.getColor(
                        "2",
                        ContextCompat.getColor(pieChart.context, R.color.red)
                    )
                )
                legend.add("2 (${(twoCount.toFloat() / sum * 100).toInt()}%)")
            }

            pieChart.legend.setCustom(legend.mapIndexed { i, value ->
                LegendEntry(value, Legend.LegendForm.DEFAULT, 10f, 2f, null, colors[i])
            })
            pieChart.centerText = pieChart.resources.getString(
                R.string.in_total,
                (fiveCount + fourCount + threeCount + twoCount).toString()
            )
            pieChart.setCenterTextSize(14f)
            pieChart.setCenterTextColor(ContextCompat.getColor(pieChart.context, R.color.text))
            pieChart.legend.textColor = ContextCompat.getColor(pieChart.context, R.color.text)

            val dataSet = PieDataSet(entries, "").apply {
                valueTextSize = 16f
                setColors(colors.reversed())
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

        override fun isLine() = false
    }

    data class PieFinalMarks(
        private val fiveCount: Int,
        private val fourCount: Int,
        private val threeCount: Int,
        private val twoCount: Int,
    ) : AnalyticsUi {
        override fun same(item: AnalyticsUi) = item is PieFinalMarks

        override fun showData(pieChart: PieChart, colorManager: ColorManager) {
            val sum = fiveCount + fourCount + threeCount + twoCount
            val entries = hashMapOf(
                "5" to fiveCount,
                "4" to fourCount,
                "3" to threeCount,
                "2" to twoCount
            ).mapNotNull {
                if (it.value == 0) null
                else
                    PieEntry(it.value.toFloat(), it.key)
            }
            val colors = arrayListOf<Int>()
            val legend = mutableListOf<String>()
            if (fiveCount > 0) {
                colors.add(
                    colorManager.getColor(
                        "5",
                        ContextCompat.getColor(pieChart.context, R.color.light_green)
                    )
                )
                legend.add("5 (${(fiveCount.toFloat() / sum * 100).toInt()}%)")
            }
            if (fourCount > 0) {
                colors.add(
                    colorManager.getColor(
                        "4",
                        ContextCompat.getColor(pieChart.context, R.color.green)
                    )
                )
                legend.add("4 (${(fourCount.toFloat() / sum * 100).toInt()}%)")
            }
            if (threeCount > 0) {
                colors.add(
                    colorManager.getColor(
                        "3",
                        ContextCompat.getColor(pieChart.context, R.color.yellow)
                    )
                )
                legend.add("3 (${(threeCount.toFloat() / sum * 100).toInt()}%)")
            }
            if (twoCount > 0) {
                colors.add(
                    colorManager.getColor(
                        "2",
                        ContextCompat.getColor(pieChart.context, R.color.red)
                    )
                )
                legend.add("2 (${(twoCount.toFloat() / sum * 100).toInt()}%)")
            }

            pieChart.legend.setCustom(legend.mapIndexed { i, value ->
                LegendEntry(value, Legend.LegendForm.DEFAULT, 10f, 2f, null, colors[i])
            })
            pieChart.centerText = pieChart.resources.getString(
                R.string.in_total,
                (fiveCount + fourCount + threeCount + twoCount).toString()
            )
            pieChart.setCenterTextSize(14f)
            pieChart.setCenterTextColor(ContextCompat.getColor(pieChart.context, R.color.text))
            pieChart.legend.textColor = ContextCompat.getColor(pieChart.context, R.color.text)

            val dataSet = PieDataSet(entries, "").apply {
                valueTextSize = 16f
                setColors(colors.reversed())
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
            val text = textView.resources.getString(R.string.final_marks_pie)
            textView.text = text
        }

        override fun isLine() = false
    }

    object Error : AnalyticsUi {
        override fun same(item: AnalyticsUi) = item is java.lang.Error

        override fun showData(lineChart: LineChart, colorManager: ColorManager) {
            lineChart.setNoDataText("Fail")
            lineChart.data = null
        }

        override fun showTitle(textView: TextView) {
            textView.text = textView.resources.getString(R.string.error)
        }

        override fun isLine() = true
    }
}