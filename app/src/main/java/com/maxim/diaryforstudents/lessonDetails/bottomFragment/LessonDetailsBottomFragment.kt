package com.maxim.diaryforstudents.lessonDetails.bottomFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maxim.diaryforstudents.core.sl.ProvideViewModel
import com.maxim.diaryforstudents.databinding.FragmentLessonDetailsBinding
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsViewModel

class LessonDetailsBottomFragment: BottomSheetDialogFragment() {
    private var _binding: FragmentLessonDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LessonDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as ProvideViewModel).viewModel(LessonDetailsViewModel::class.java)



        viewModel.init()

        binding.titleLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clear()
    }
}