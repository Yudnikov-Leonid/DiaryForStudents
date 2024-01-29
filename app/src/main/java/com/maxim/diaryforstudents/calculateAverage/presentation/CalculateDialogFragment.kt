package com.maxim.diaryforstudents.calculateAverage.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import com.maxim.diaryforstudents.databinding.DialogFragmentCalculateAverageBinding
import com.maxim.diaryforstudents.performance.presentation.PerformanceMarksAdapter

class CalculateDialogFragment: DialogFragment() {
    private var _binding: DialogFragmentCalculateAverageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CalculateViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFragmentCalculateAverageBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext()).setView(binding.root)

        viewModel = (requireActivity() as ProvideViewModel).viewModel(CalculateViewModel::class.java)

        val adapter = PerformanceMarksAdapter()
        binding.marksRecyclerView.adapter = adapter

        viewModel.observe(this) {
            it.show(adapter, binding.averageTextView)
        }

        viewModel.init()

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clear()
    }
}