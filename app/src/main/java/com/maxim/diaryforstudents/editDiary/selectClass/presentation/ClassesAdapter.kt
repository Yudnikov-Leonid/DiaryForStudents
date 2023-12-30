package com.maxim.diaryforstudents.editDiary.selectClass.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.ClassLayoutBinding
import com.maxim.diaryforstudents.databinding.ClassesNoDataBinding

class ClassesAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<ClassesAdapter.ItemViewHolder>() {
    private val list = mutableListOf<ClassUi>()

    abstract class ItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: ClassUi, listener: Listener) {}
    }

    class BaseViewHolder(private val binding: ClassLayoutBinding) : ItemViewHolder(binding) {
        override fun bind(item: ClassUi, listener: Listener) {
            item.show(binding.classNameTextView)
            itemView.setOnClickListener {
                item.open(listener)
            }
        }
    }

    class EmptyViewHolder(binding: ClassesNoDataBinding) : ItemViewHolder(binding)

    override fun getItemViewType(position: Int) = if (list[position] is ClassUi.Base) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return if (viewType == 0) BaseViewHolder(
            ClassLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        ) else EmptyViewHolder(
            ClassesNoDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    fun update(newList: List<ClassUi>) {
        val diff = ClassesDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun openClass(id: String)
    }
}

class ClassesDiffUtil(
    private val oldList: List<ClassUi>,
    private val newList: List<ClassUi>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}