package com.maxim.diaryforstudents.performance.common.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxim.diaryforstudents.core.ProvideColorManager
import com.maxim.diaryforstudents.databinding.MarkBinding

class PerformanceMarksAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<PerformanceMarksAdapter.ItemViewHolder>() {
    private val list = mutableListOf<PerformanceUi>()
    private var showDate = true
    private var showType = true

    class ItemViewHolder(private val binding: MarkBinding, private val listener: Listener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PerformanceUi, showDate: Boolean, showType: Boolean) {
            binding.dateTextView.visibility = if (showDate) View.VISIBLE else View.GONE
            item.showName(
                binding.markTextView,
                (binding.markTextView.context.applicationContext as ProvideColorManager).colorManager()
            )
            item.showDate(binding.dateTextView)
            if (showType)
                item.showType(binding.root)
            val colorManager =
                (binding.root.context.applicationContext as ProvideColorManager).colorManager()
            item.showIsChecked(binding.root, colorManager)
            itemView.setOnClickListener {
                item.openDetails(listener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            MarkBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], showDate, showType)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<PerformanceUi>, showDate: Boolean, showType: Boolean) {
        this.showDate = showDate
        this.showType = showType
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    interface Listener {
        fun details(mark: PerformanceUi)
    }
}