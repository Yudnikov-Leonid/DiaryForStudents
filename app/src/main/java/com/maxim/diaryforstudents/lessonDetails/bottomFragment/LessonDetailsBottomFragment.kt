package com.maxim.diaryforstudents.lessonDetails.bottomFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maxim.diaryforstudents.databinding.FragmentLessonDetailsBinding
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsViewModel

class LessonDetailsBottomFragment: BottomSheetDialogFragment() {
    private var _binding: FragmentLessonDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LessonDetailsViewModel by viewModels()

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

        viewModel.observe(this) {
            it.show(
                binding.lessonNameTextView,
                binding.lessonNumberTextView,
                binding.teacherNameTextView,
                binding.topicTextView,
                binding.topicTitleTextView,
                binding.homeworkTextView,
                binding.homeworkTitleTextView,
                binding.previousHomeworkTextView,
                binding.previousHomeworkTitleTextView,
                binding.marksLayout,
                binding.markTypeTextView,
                binding.noteTextView,
                binding.noteTitleTextView,
                binding.progressBar
            )
        }

        viewModel.init()

        binding.titleLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.clear()
    }
}