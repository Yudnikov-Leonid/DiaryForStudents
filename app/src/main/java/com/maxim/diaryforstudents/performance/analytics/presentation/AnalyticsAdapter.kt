package com.maxim.diaryforstudents.performance.analytics.presentation

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.mikephil.charting.components.XAxis
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.databinding.LineChartLayoutBinding

class AnalyticsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<AnalyticsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<AnalyticsUi>()

    class ItemViewHolder(
        private val binding: LineChartLayoutBinding,
        private val listener: Listener
    ) : ViewHolder(binding.root) {
        fun bind(item: AnalyticsUi) {
            item.showData(binding.chart)
            var byUser = false
            val listener = object : AdapterView.OnItemSelectedListener, View.OnTouchListener {

                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    byUser = true
                    return false
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (byUser)
                        listener.changeQuarter(position + 1)
                    byUser = false
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
            binding.quarterSpinner.onItemSelectedListener = listener
            binding.quarterSpinner.setOnTouchListener(listener)


            binding.chart.apply {
                setDrawBorders(false)
                isDoubleTapToZoomEnabled = false
                setScaleEnabled(false)
                setTouchEnabled(false)
                isAutoScaleMinMaxEnabled = true
                legend.isEnabled = false
                description.isEnabled = false
            }

            binding.chart.axisLeft.apply {
                axisLineWidth = 4f
                axisLineColor = ContextCompat.getColor(binding.chart.context, R.color.back_blue)
                labelCount = 4
                setDrawGridLines(false)
                textSize = 14f
                textColor = ContextCompat.getColor(binding.chart.context, R.color.green)
                typeface = Typeface.DEFAULT_BOLD
            }

            binding.chart.axisRight.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
                setDrawAxisLine(false)
            }

            binding.chart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                axisLineColor = ContextCompat.getColor(binding.chart.context, R.color.back_blue)
                axisLineWidth = 4f
                setDrawGridLines(false)
                textColor = ContextCompat.getColor(binding.chart.context, R.color.dark_gray)
                typeface = Typeface.DEFAULT_BOLD
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LineChartLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<AnalyticsUi>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    interface Listener {
        fun changeQuarter(value: Int)
    }
}