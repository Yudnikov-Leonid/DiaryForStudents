package com.maxim.diaryforstudents.diary.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.databinding.DayBinding

class DiaryDaysAdapter : RecyclerView.Adapter<DiaryDaysAdapter.ItemViewHolder>() {
    private val list = mutableListOf<DayUi>()

    class ItemViewHolder(private val binding: DayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DayUi) {
            item.showDayOfTheWeek(binding.dayTextView)
            item.showDate(binding.dateTextView)
            item.setSelectedColor(itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(DayBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<DayUi>) {
        val diff = DiaryDayDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}

class DiaryDayDiffUtil(
    private val oldList: List<DayUi>,
    private val newList: List<DayUi>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = false
}