package com.maxim.diaryforstudents.analytics.presentation

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.components.XAxis
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.databinding.AnalyticsTitleBinding
import com.maxim.diaryforstudents.databinding.LineChartLayoutBinding
import com.maxim.diaryforstudents.databinding.PieChartLayoutBinding

class AnalyticsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<AnalyticsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<AnalyticsUi>()

    abstract class ItemViewHolder(binding: ViewBinding) : ViewHolder(binding.root) {
        abstract fun bind(item: AnalyticsUi, showSpinner: Boolean)
    }

    class LineViewHolder(
        private val binding: LineChartLayoutBinding,
        private val listener: Listener
    ) : ItemViewHolder(binding) {
        override fun bind(item: AnalyticsUi, showSpinner: Boolean) {
            item.showData(binding.chart)
            item.showTitle(binding.titleTextView)
            item.showQuarter(binding.quarterSpinner)
            item.showInterval(binding.intervalSpinner)
            val visibility = if (showSpinner) View.VISIBLE else View.GONE
            binding.quarterSpinner.visibility = visibility
            binding.intervalLayout.visibility = visibility
            if (showSpinner) {
                var byUser = false
                val periodListener =
                    object : AdapterView.OnItemSelectedListener, View.OnTouchListener {
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

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            byUser = false
                        }
                    }
                binding.quarterSpinner.onItemSelectedListener = periodListener
                binding.quarterSpinner.setOnTouchListener(periodListener)

                val intervalListener =
                    object : AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                                listener.changeInterval(
                                    when (position) {
                                        0 -> 1
                                        1 -> 2
                                        2 -> 3
                                        3 -> 7
                                        else -> 28
                                    }
                                )
                            byUser = false
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            byUser = false
                        }
                    }
                binding.intervalSpinner.onItemSelectedListener = intervalListener
                binding.intervalSpinner.setOnTouchListener(intervalListener)
            }



            binding.chart.apply {
                setDrawBorders(false)
                isDoubleTapToZoomEnabled = false
                setScaleEnabled(true)
                setTouchEnabled(true)
                isAutoScaleMinMaxEnabled = true
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

    class PieViewHolder(private val binding: PieChartLayoutBinding) : ItemViewHolder(binding) {
        override fun bind(item: AnalyticsUi, showSpinner: Boolean) {
            binding.chart.apply {
                description.isEnabled = false
                setDrawEntryLabels(false)
                setHoleColor(ContextCompat.getColor(binding.chart.context, R.color.background))
            }
            item.showData(binding.chart)
            item.showTitle(binding.titleTextView)
        }
    }

    class TitleViewHolder(private val binding: AnalyticsTitleBinding, private val listener: Listener) : ItemViewHolder(binding) {
        override fun bind(item: AnalyticsUi, showSpinner: Boolean) {
            item.showTitle(binding.titleTextView)
            binding.backButton.setOnClickListener {
                listener.goBack()
            }
        }
    }

    override fun getItemViewType(position: Int) =
        if (list[position].isLine()) 0 else if (list[position] is AnalyticsUi.Title) 2 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when (viewType) {
            0 -> LineViewHolder(
                LineChartLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), listener
            )

            1 -> PieViewHolder(
                PieChartLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> TitleViewHolder(
                AnalyticsTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), listener
            )
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], position == 1)
    }

    fun update(newList: List<AnalyticsUi>) {
        val diff = AnalyticsDiff(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun changeQuarter(value: Int)
        fun changeInterval(value: Int)
        fun goBack()
    }
}

class AnalyticsDiff(
    private val oldList: List<AnalyticsUi>,
    private val newList: List<AnalyticsUi>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}