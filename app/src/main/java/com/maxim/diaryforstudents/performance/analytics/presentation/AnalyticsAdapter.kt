package com.maxim.diaryforstudents.performance.analytics.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxim.diaryforstudents.databinding.LineChartLayoutBinding

class AnalyticsAdapter : RecyclerView.Adapter<AnalyticsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<AnalyticsUi>()

    class ItemViewHolder(private val binding: LineChartLayoutBinding) : ViewHolder(binding.root) {
        fun bind(item: AnalyticsUi) {
            item.showData(binding.chart)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LineChartLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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
}