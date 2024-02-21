package com.maxim.diaryforstudents.news.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.maxim.diaryforstudents.databinding.BaseNewsBinding
import com.maxim.diaryforstudents.databinding.EmptyNewsBinding
import com.maxim.diaryforstudents.databinding.FailureNewsBinding
import com.maxim.diaryforstudents.databinding.NewsMainLayourBinding

class NewsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<NewsAdapter.ItemViewHolder>() {
    private val list = mutableListOf<NewsUi>()

    abstract class ItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: NewsUi) {}
    }

    class MainViewHolder(
        private val binding: NewsMainLayourBinding,
        private val listener: Listener
    ) : ItemViewHolder(binding) {
        override fun bind(item: NewsUi) {
            item.showTitle(binding.titleTextView)
            item.showContent(binding.contentTextView)
            item.showImage(binding.imageView)
            item.showDate(binding.dateTextView)
            itemView.setOnClickListener {
                listener.open(item)
            }
        }
    }

    class BaseViewHolder(
        private val binding: BaseNewsBinding,
        private val listener: Listener
    ) : ItemViewHolder(binding) {
        override fun bind(item: NewsUi) {
            item.showTitle(binding.titleTextView)
            item.showDate(binding.dateTextView)
            itemView.setOnClickListener {
                listener.open(item)
            }
        }
    }

    class EmptyViewHolder(binding: EmptyNewsBinding) : ItemViewHolder(binding)

    class FailureViewHolder(
        private val binding: FailureNewsBinding,
        private val listener: Listener
    ) : ItemViewHolder(binding) {
        override fun bind(item: NewsUi) {
            item.showTitle(binding.errorTextView)
            binding.retryButton.setOnClickListener {
                listener.retry()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is NewsUi.Base) 0 else if (list[position] is NewsUi.Main) 1
        else if (list[position] is NewsUi.Empty) 2 else 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when (viewType) {
            0 -> BaseViewHolder(
                BaseNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                listener
            )

            1 -> MainViewHolder(
                NewsMainLayourBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                listener
            )

            2 -> EmptyViewHolder(
                EmptyNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> FailureViewHolder(
                FailureNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                listener
            )
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(newList: List<NewsUi>) {
        val diff = NewsDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    interface Listener {
        fun retry()
        fun open(value: NewsUi)
    }
}

class NewsDiffUtil(
    private val oldList: List<NewsUi>,
    private val newList: List<NewsUi>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].sameContent(newList[newItemPosition])
}