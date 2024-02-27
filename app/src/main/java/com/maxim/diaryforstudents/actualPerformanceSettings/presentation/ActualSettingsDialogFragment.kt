package com.maxim.diaryforstudents.actualPerformanceSettings.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.DialogFragment
import com.maxim.diaryforstudents.core.presentation.SerializableLambda
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

        val reload = requireArguments().getSerializable(KEY) as SerializableLambda

        viewModel.init(
            binding.showProgressSwitch,
            binding.showTypeSwitch,
            binding.progressInfoLayout,
            binding.typeInfoLayout,
            binding.progressCompareSpinner,
            binding.sortBySpinner,
            binding.sortingOrderSpinner
        )

        binding.showProgressSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setShowProgress(isChecked, reload)
            binding.progressInfoLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.showTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setShowType(isChecked, reload)
            binding.typeInfoLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.progressCompareSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setProgressCompared(position, reload)
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
                viewModel.setSortBy(position, reload)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        binding.sortingOrderSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setSortingOrder(position, reload)
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

    companion object {
        private const val KEY = "actual_settings_reload_key"

        fun newInstance(reload: SerializableLambda): ActualSettingsDialogFragment {
            return ActualSettingsDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY, reload)
                }
            }
        }
    }
}