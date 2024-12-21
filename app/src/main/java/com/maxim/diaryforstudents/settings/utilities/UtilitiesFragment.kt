package com.maxim.diaryforstudents.settings.utilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.ProvideColorManager
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentSettingsUtilitiesBinding
import yuku.ambilwarna.AmbilWarnaDialog

class UtilitiesFragment : BaseFragment<FragmentSettingsUtilitiesBinding, UtilitiesViewModel>() {
    override val viewModelClass = UtilitiesViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsUtilitiesBinding.inflate(inflater, container, false)

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

        binding.openMonday.init()
        binding.showFirstGraph.init()
        binding.showSecondGraph.init()
        binding.showThirdGraph.init()
        binding.showFourthGraph.init()
    }
}