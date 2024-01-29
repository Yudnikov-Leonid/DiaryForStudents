package com.maxim.diaryforstudents.actualPerformanceSettings.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.DialogFragment
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import com.maxim.diaryforstudents.databinding.FragmentActualPerformanceSettingsBinding

class ActualSettingsDialogFragment : DialogFragment() {
    private var _binding: FragmentActualPerformanceSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ActualSettingsViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentActualPerformanceSettingsBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext()).setView(binding.root)

        viewModel =
            (requireActivity() as ProvideViewModel).viewModel(ActualSettingsViewModel::class.java)

        viewModel.init(
            binding.showProgressSwitch,
            binding.progressInfoLayout,
            binding.progressCompareSpinner,
            binding.sortBySpinner
        )

        binding.showProgressSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setShowProgress(isChecked)
            binding.progressInfoLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.progressCompareSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setProgressCompared(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        binding.sortBySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setSortBy(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.close()
    }
}