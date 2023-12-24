package com.maxim.diaryforstudents.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.diaryforstudents.core.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentNewsBinding

class NewsFragment: BaseFragment<FragmentNewsBinding, NewsViewModel>() {
    override val viewModelClass: Class<NewsViewModel>
        get() = NewsViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentNewsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsAdapter(object : NewsAdapter.Listener {
            override fun retry() {
                viewModel.reload()
            }
        })
        binding.newsRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.showList(adapter)
        }

        viewModel.init(savedInstanceState == null)
    }
}