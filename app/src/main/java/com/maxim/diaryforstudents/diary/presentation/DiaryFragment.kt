package com.maxim.diaryforstudents.diary.presentation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.databinding.FragmentDiaryBinding
import java.util.Calendar

class DiaryFragment : BaseFragment<FragmentDiaryBinding, DiaryViewModel>() {
    override val viewModelClass: Class<DiaryViewModel>
        get() = DiaryViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryBinding.inflate(inflater, container, false)

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
        binding.errorBackButton.setOnClickListener {
            viewModel.goBack()
        }
        val lessonsAdapter = DiaryLessonsAdapter(object : DiaryLessonsAdapter.Listener {
            override fun openDetails(item: DiaryUi.Lesson) {
                viewModel.openDetails(item)
            }
        })
        binding.lessonsRecyclerView.adapter = lessonsAdapter

        binding.retryButton.setOnClickListener {
            viewModel.reload(true)
        }

        binding.selectDateButton.setOnClickListener {
            val actualDay = viewModel.actualDay()

            val year = Formatter.Base.format("yyyy", actualDay).toInt()
            val month = Formatter.Base.format("MM", actualDay).toInt() - 1
            val day = Formatter.Base.format("dd", actualDay).toInt()
            DatePickerDialog(requireContext(), { _, yearValue, monthValue, dayOfMonthValue ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, yearValue)
                calendar.set(Calendar.MONTH, monthValue)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonthValue)
                viewModel.setActualDay((calendar.timeInMillis / 86400000).toInt())
            }, year, month, day).show()
        }

        //todo refactor with share interface
        binding.shareDateButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
            }
            AlertDialog.Builder(requireContext())
                .setTitle(requireContext().resources.getString(R.string.share_homework))
                .setItems(
                    arrayOf(
                        requireContext().resources.getString(R.string.send_actual),
                        requireContext().resources.getString(R.string.send_previous)
                    )
                ) { _, i ->
                    if (i == 0) {
                        intent.putExtra(Intent.EXTRA_TEXT, viewModel.homeworkToShare())
                        startActivity(
                            Intent.createChooser(
                                intent,
                                requireContext().getString(R.string.send_to)
                            )
                        )
                    } else {
                        intent.putExtra(Intent.EXTRA_TEXT, viewModel.previousHomeworkToShare())
                        startActivity(
                            Intent.createChooser(
                                intent,
                                requireContext().getString(R.string.send_to)
                            )
                        )
                    }
                }.create().show()
        }

        val daysAdapter = DaysAdapter(requireActivity())
        binding.daysViewPager.adapter = daysAdapter
        binding.daysViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (positionOffset == 0f) {
                    if (position == 0) {
                        viewModel.previousWeek()
                    }
                    else if (position == 2) {
                        viewModel.nextWeek()
                    }
                }
            }
        })

        viewModel.observe(this) {
            it.show(
                lessonsAdapter,
                daysAdapter,
                binding.topLayout,
                binding.monthTextView,
                binding.progressBar,
                binding.errorTextView,
                binding.retryButton,
                binding.errorBackButton,
                binding.lessonsRecyclerView,
            )
            binding.daysViewPager.setCurrentItem(1, false)
            binding.lessonsRecyclerView.scrollToPosition(0)
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