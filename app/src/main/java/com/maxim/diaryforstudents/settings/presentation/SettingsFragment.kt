package com.maxim.diaryforstudents.settings.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentSettingsBinding


class SettingsFragment: BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {
    override val viewModelClass = SettingsViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(inflater, container, false)

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

        binding.themesButton.setOnClickListener {
            viewModel.themes()
        }

        binding.utilitiesButton.setOnClickListener {
            viewModel.utilities()
        }

        binding.contactsButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/dorhun"))
            startActivity(browserIntent)
        }
    }
}