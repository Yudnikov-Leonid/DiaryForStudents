package com.maxim.diaryforstudents.performance.presentation

import androidx.recyclerview.widget.DiffUtil

class PerformanceDiffUtil(
    private val oldList: List<PerformanceUi>,
    private val newList: List<PerformanceUi>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].sameContent(newList[newItemPosition])
}