package com.maxim.diaryforstudents.diary.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentDiaryBinding

class DiaryFragment : BaseFragment<FragmentDiaryBinding, DiaryViewModel>() {
    override val viewModelClass: Class<DiaryViewModel>
        get() = DiaryViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.back()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        val lessonsAdapter = DiaryLessonsAdapter()
        binding.lessonsRecyclerView.adapter = lessonsAdapter

        val daysAdapter = DiaryDaysAdapter(object : DiaryDaysAdapter.Listener {
            override fun selectDay(day: Int) {
                viewModel.setActualDay(day)
            }
        })
        binding.daysRecyclerView.adapter = daysAdapter

        binding.moveLeftButton.setOnClickListener {
            viewModel.previousDay()
        }
        binding.moveRightButton.setOnClickListener {
            viewModel.nextDay()
        }
        //todo !!
        binding.shareHomeworkButton!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type="text/plain"
                putExtra(Intent.EXTRA_TEXT, viewModel.homeworkToShare())
            }
            startActivity(Intent.createChooser(intent, "Send to"))
        }

        viewModel.observe(this) {
            it.show(
                lessonsAdapter,
                daysAdapter,
                binding.shareHomeworkButton!!,
                binding.monthTextView,
                binding.progressBar,
                binding.errorTextView,
                binding.moveLeftButton,
                binding.moveRightButton,
                binding.daysRecyclerView,
                binding.lessonsRecyclerView
            )
        }
        viewModel.init(savedInstanceState == null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }
    }
}