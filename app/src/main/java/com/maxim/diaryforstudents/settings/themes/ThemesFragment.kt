package com.maxim.diaryforstudents.settings.themes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentSettingsThemesBinding
import dagger.hilt.android.AndroidEntryPoint
import yuku.ambilwarna.AmbilWarnaDialog

@AndroidEntryPoint
class ThemesFragment : BaseFragment<FragmentSettingsThemesBinding>(),
    OpenColorPicker {
    private val viewModel: SettingsThemesViewModel by viewModels()
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsThemesBinding.inflate(inflater, container, false)

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

        binding.fiveColorButton.setOnClickListener {
            viewModel.openColorPicker("5", R.color.light_green, this)
        }

        binding.fourColorButton.setOnClickListener {
            viewModel.openColorPicker("4", R.color.green, this)
        }

        binding.threeColorButton.setOnClickListener {
            viewModel.openColorPicker("3", R.color.yellow, this)
        }

        binding.twoColorButton.setOnClickListener {
            viewModel.openColorPicker("2", R.color.red, this)
        }

        binding.oneColorButton.setOnClickListener {
            viewModel.openColorPicker("1", R.color.red, this)
        }

        binding.fiveResetButton.setOnClickListener {
            viewModel.resetColor("5")
        }

        binding.fourResetButton.setOnClickListener {
            viewModel.resetColor("4")
        }

        binding.threeResetButton.setOnClickListener {
            viewModel.resetColor("3")
        }

        binding.twoResetButton.setOnClickListener {
            viewModel.resetColor("2")
        }

        binding.oneResetButton.setOnClickListener {
            viewModel.resetColor("1")
        }

        binding.showLessonsInMenuSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setShowLessonsInMenu(isChecked)
        }

        viewModel.observe(this) {
            it.show(
                binding.fiveColorButton,
                binding.fourColorButton,
                binding.threeColorButton,
                binding.twoColorButton,
                binding.oneColorButton,
                viewModel.colorManager,
                binding.showLessonsInMenuSwitch
            )
        }

        viewModel.reload()
    }

    override fun open(defaultColor: Int, parseColor: Boolean, key: String) {
        AmbilWarnaDialog(
            requireContext(),
            if (parseColor) ContextCompat.getColor(requireContext(), defaultColor) else defaultColor,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) = Unit

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    viewModel.saveColor(color, key)
                }
            }).show()
    }
}

interface OpenColorPicker {
    fun open(defaultColor: Int, parseColor: Boolean, key: String)
}