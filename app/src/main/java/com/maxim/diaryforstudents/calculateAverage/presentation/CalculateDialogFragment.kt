package com.maxim.diaryforstudents.calculateAverage.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import com.maxim.diaryforstudents.databinding.DialogFragmentCalculateAverageBinding
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceMarksAdapter
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

class CalculateDialogFragment: DialogFragment() {
    private var _binding: DialogFragmentCalculateAverageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CalculateViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFragmentCalculateAverageBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext()).setView(binding.root)

        viewModel = (requireActivity() as ProvideViewModel).viewModel(CalculateViewModel::class.java)

        val adapter = PerformanceMarksAdapter(object : PerformanceMarksAdapter.Listener {
            override fun details(mark: PerformanceUi.Mark) = Unit
        })
        binding.marksRecyclerView.adapter = adapter
        binding.marksRecyclerView.itemAnimator = null

        binding.addMarkLinearLayout.children.forEachIndexed { i, view ->
            view.setOnClickListener {
                viewModel.add(5 - i)
            }
        }

        binding.removeMarkLinearLayout.children.forEachIndexed { i, view ->
            view.setOnClickListener {
                viewModel.remove(5 - i)
            }
        }

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