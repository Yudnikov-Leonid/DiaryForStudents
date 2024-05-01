package com.maxim.diaryforstudents.news.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>() {
    private val viewModel: NewsViewModel by viewModels()

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentNewsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            viewModel.goBack()
        }

        val listener = object : NewsAdapter.Listener {
            override fun open(value: NewsUi) {
                viewModel.open(value)
            }
        }
        val importantAdapter = NewsAdapter(listener)
        binding.importantNewsRecyclerView.adapter = importantAdapter

        val defaultAdapter = NewsAdapter(listener)
        binding.defaultNewsRecyclerView.adapter = defaultAdapter

        binding.retryButton.setOnClickListener {
            viewModel.init(true)
        }

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
            )
            it.show(
                binding.errorTextView,
                binding.retryButton,
                binding.news,
                binding.skeletonLoading
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