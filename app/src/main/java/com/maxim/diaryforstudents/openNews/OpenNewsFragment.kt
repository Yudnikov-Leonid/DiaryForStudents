package com.maxim.diaryforstudents.openNews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentOpenNewsBinding

class OpenNewsFragment: BaseFragment<FragmentOpenNewsBinding, OpenNewsViewModel>() {
    override val viewModelClass: Class<OpenNewsViewModel>
        get() = OpenNewsViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOpenNewsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.back()
            }
        }
        super.onViewCreated(view, savedInstanceState)
        val data = viewModel.data()
        data.showTitle(binding.titleTextView)
        data.showDate(binding.dateTextView)
        data.showContent(binding.contentTextView)
    }
}