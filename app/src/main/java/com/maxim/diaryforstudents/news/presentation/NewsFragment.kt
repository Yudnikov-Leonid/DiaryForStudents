package com.maxim.diaryforstudents.news.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentNewsBinding

class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>() {
    override val viewModelClass = NewsViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentNewsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)


        val listener = object : NewsAdapter.Listener {
            override fun retry() {
                viewModel.init(true)
            }

            override fun open(value: NewsUi) {
                viewModel.open(value)
            }
        }
        val importantAdapter = NewsAdapter(listener)
        binding.importantNewsRecyclerView.adapter = importantAdapter

        val defaultAdapter = NewsAdapter(listener)
        binding.defaultNewsRecyclerView.adapter = defaultAdapter

        viewModel.observe(this) {
            it.show(
                binding.mainNews,
                binding.imageView,
                binding.titleTextView,
                binding.contentTextView,
                binding.dateTextView,
                importantAdapter,
                defaultAdapter,
                listener,
                binding.progressBar
            )
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }
    }
}