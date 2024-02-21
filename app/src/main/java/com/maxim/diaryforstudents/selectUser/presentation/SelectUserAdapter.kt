package com.maxim.diaryforstudents.selectUser.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.AnalyticsTitleBinding
import com.maxim.diaryforstudents.databinding.SelectUserLayoutBinding

class SelectUserAdapter(private val listener: Listener) :
    RecyclerView.Adapter<SelectUserAdapter.ItemViewHolder>() {
    private val list = mutableListOf<SelectUserUi>()

    abstract class ItemViewHolder(
        binding: ViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: SelectUserUi, position: Int)
    }

    class UserViewHolder(
        private val binding: SelectUserLayoutBinding,
        private val listener: Listener
    ) : ItemViewHolder(binding) {
        override fun bind(item: SelectUserUi, position: Int) {
            item.showName(binding.nameTextView)
            item.showSchool(binding.schoolNameTextView)
            itemView.setOnClickListener {
                listener.select(position - 1)
            }
        }
    }

    class TitleViewHolder(private val binding: AnalyticsTitleBinding) : ItemViewHolder(binding) {
        override fun bind(item: SelectUserUi, position: Int) {
            item.showName(binding.titleTextView)
        }
    }

    interface Listener {
        fun select(position: Int)
    }

    override fun getItemViewType(position: Int) = if (list[position] is SelectUserUi.Base) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return if (viewType == 0) UserViewHolder(
            SelectUserLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        ) else TitleViewHolder(
            AnalyticsTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<SelectUserUi>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}