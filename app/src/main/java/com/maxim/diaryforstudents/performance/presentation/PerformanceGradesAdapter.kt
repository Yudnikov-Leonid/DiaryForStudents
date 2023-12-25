package com.maxim.diaryforstudents.performance.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.databinding.GradeBinding

class PerformanceGradesAdapter : RecyclerView.Adapter<PerformanceGradesAdapter.ItemViewHolder>() {
    private val list = mutableListOf<PerformanceUi.Grade>()

    class ItemViewHolder(private val binding: GradeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PerformanceUi.Grade) {
            item.showName(binding.gradeTextView)
            item.showDate(binding.dateTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            GradeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<PerformanceUi.Grade>) {
        val diff = GradesDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}

class GradesDiffUtil(
    private val oldList: List<PerformanceUi.Grade>,
    private val newList: List<PerformanceUi.Grade>,
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].sameContent(newList[newItemPosition])
}