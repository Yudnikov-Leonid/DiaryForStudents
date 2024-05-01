package com.maxim.diaryforstudents.settings.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: BaseFragment<FragmentSettingsBinding>() {
    private val viewModel: SettingsViewModel by viewModels()
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

        binding.contactsButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/dorhun"))
            startActivity(browserIntent)
        }
    }
}