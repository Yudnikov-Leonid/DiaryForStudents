package com.maxim.diaryforstudents.openNews

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentOpenNewsBinding

class OpenNewsFragment : BaseFragment<FragmentOpenNewsBinding, OpenNewsViewModel>(), Share {
    override val viewModelClass: Class<OpenNewsViewModel>
        get() = OpenNewsViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOpenNewsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) { data ->
            data.showTitle(binding.titleTextView)
            data.showDate(binding.dateTextView)
            data.showContent(binding.contentTextView)
            data.showImage(binding.newsImage)
            data.showDownloadButton(binding.downloadButton)
        }

        binding.downloadButton.setOnClickListener {
            viewModel.download()
        }

        binding.backButton.setOnClickListener {
            viewModel.goBack()
        }

        binding.shareButton.setOnClickListener {
            viewModel.share(this)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(savedInstanceState)) }
    }

    override fun share(content: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
        }
        intent.putExtra(Intent.EXTRA_TEXT, content)
        startActivity(
            Intent.createChooser(
                intent,
                requireContext().getString(R.string.send_to)
            )
        )
    }
}

interface Share {
    fun share(content: String)
}