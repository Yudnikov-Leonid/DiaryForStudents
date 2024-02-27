package com.maxim.diaryforstudents.lessonDetails.presentation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentLessonDetailsBinding
import com.maxim.diaryforstudents.openNews.Share

class LessonDetailsFragment: BaseFragment<FragmentLessonDetailsBinding, LessonDetailsViewModel>(), Share {
    override val viewModelClass = LessonDetailsViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLessonDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }

        binding.backButton.setOnClickListener {
            viewModel.goBack()
        }

        binding.shareButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(requireContext().resources.getString(R.string.share_homework))
                .setItems(
                    arrayOf(
                        requireContext().resources.getString(R.string.send_actual),
                        requireContext().resources.getString(R.string.send_previous),
                        requireContext().resources.getString(R.string.send_all)
                    )
                ) { _, i ->
                    viewModel.share(this, when (i) {
                        0 -> ShareType.Actual
                        1 -> ShareType.Previous
                        else -> ShareType.All
                    })
                }.create().show()
        }

        viewModel.observe(this) {
            it.show(
                binding.lessonNameTextView,
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun share(content: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
        }
        intent.putExtra(Intent.EXTRA_TEXT, content)
        startActivity(
            Intent.createChooser(
                intent,
                requireContext().getString(R.string.send_to)
            )
        )
    }
}