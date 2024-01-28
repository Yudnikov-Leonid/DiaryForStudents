package com.maxim.diaryforstudents.performance.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.databinding.MarkBinding

class PerformanceMarksAdapter : RecyclerView.Adapter<PerformanceMarksAdapter.ItemViewHolder>() {
    private val list = mutableListOf<PerformanceUi.Mark>()

    class ItemViewHolder(private val binding: MarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PerformanceUi.Mark) {
            item.showName(binding.markTextView)
            item.showDate(binding.dateTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            MarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<PerformanceUi.Mark>) {
        val diff = PerformanceDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}